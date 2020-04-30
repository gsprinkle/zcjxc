package com.ischoolbar.programmer.jxc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ischoolbar.programmer.entity.admin.User;
import com.ischoolbar.programmer.jxc.entity.Customer;
import com.ischoolbar.programmer.jxc.entity.Inventory;
import com.ischoolbar.programmer.jxc.entity.Product;
import com.ischoolbar.programmer.jxc.entity.Sale;
import com.ischoolbar.programmer.jxc.entity.Stock;
import com.ischoolbar.programmer.jxc.mapper.InventoryMapper;
import com.ischoolbar.programmer.jxc.mapper.ProductMapper;
import com.ischoolbar.programmer.jxc.mapper.SaleMapper;
import com.ischoolbar.programmer.jxc.mapper.StockMapper;
import com.ischoolbar.programmer.jxc.pojo.ProductBrandCategoryVo;
import com.ischoolbar.programmer.jxc.service.IProductService;
import com.ischoolbar.programmer.service.admin.LogService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {
	@Autowired
	LogService logService;
	
	@Autowired
	SaleMapper saleMapper;
	@Autowired
	StockMapper stockMapper;
	
	
	@Autowired
	InventoryMapper inventoryMapper;

	@Override
	public Page<ProductBrandCategoryVo> selectPageVo(Page<ProductBrandCategoryVo> page, Product product) {
		return baseMapper.selectPageVo(page, product);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> addOrUpdate(Product product, HttpServletRequest request) {
		boolean hasId = product.getProductId() == null ? false : true;
		Map<String, Object> ret = new HashMap<>();
		// 数据校验
		if (StringUtils.isEmpty(product.getProductName())) {
			ret.put("type", "error");
			ret.put("msg", "产品名不能为空");
			return ret;
		}
		if (!hasId && isExist(product.getProductName())) {
			ret.put("type", "error");
			ret.put("msg", "该产品已经存在，请重新输入！");
			return ret;
		}
		// 获取登录信息
		User user = (User) request.getSession().getAttribute("user");
		// 没有ID表示新增，记录日志
		if (!hasId) {
			if (baseMapper.insert(product) > 0) {
				// 记录日志
				logService.add("用户【" + user.getUsername() + "】添加了产品 {" + product + "}");
				// 库存中添加相应的商品
				List<Inventory> list = inventoryMapper.selectList(new QueryWrapper<Inventory>().eq("product_id", product.getProductId()));
				if (list == null || list.size() < 1) {
					Inventory inv = new Inventory();
					inv.setInventoryNum(0);
					inv.setProductId(product.getProductId());
					inventoryMapper.insert(inv);
					// 记录日志
					logService.add("用户【" + user.getUsername() + "】添加产品后系统添加了库存 {" + inv + "}");
				}
			} else {
				ret.put("type", "error");
				ret.put("msg", "新增产品异常，请联系管理员！");
				return ret;
			}
		} else {// 有ID，表示修改,修改成功 ，记录日志
			Product oldProd = baseMapper.selectById(product.getProductId());
			if (baseMapper.updateById(product) > 0) {
				product = baseMapper.selectById(product.getProductId());
				logService.add("用户【" + user.getUsername() + "】修改产品 {" + oldProd + "}为：-->>" + product);
			} else {
				ret.put("type", "error");
				ret.put("msg", "修改产品异常，请联系管理员！");
				return ret;
			}
		}
		ret.put("type", "success");
		ret.put("msg", hasId ? "产品修改成功！" : "产品添加成功！");
		return ret;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> delete(Integer productId, HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<>();
		// 根据productId查询销售列表和进货列表
		List<Sale> saleList = saleMapper.selectList(new QueryWrapper<Sale>().eq("product_id", productId));
		// 如果产品有销售和进货记录，提示无法删除；
		List<Stock> stockList = stockMapper.selectList(new QueryWrapper<Stock>().eq("product_id", productId));
		// 库存是否为零
		Inventory inv = inventoryMapper.selectOne(new QueryWrapper<Inventory>().eq("product_id", productId));
			
		if ((inv != null && inv.getInventoryNum() > 0) || (saleList != null && saleList.size() > 0) || (stockList != null && stockList.size() > 0)) {
			ret.put("type", "error");
			ret.put("msg", "该产品在销售、进货 中存在记录，或库存数量不为0，无法删除！");
			return ret;
		}
		
		// 查询所要删除的产品，用于日志记录
		Product product = baseMapper.selectById(productId);
		// 如果没有，直接删除
		if (baseMapper.deleteById(productId) < 1) {
			ret.put("type", "error");
			ret.put("msg", "删除产品异常，请联系管理员！");
			return ret;
		}
		// 删除库存
		inventoryMapper.deleteById(inv);
		// 记录日志：先获取登录的用户
		User user = (User) request.getSession().getAttribute("user");
		// 写入日志
		logService.add("用户【" + user.getUsername() + "】删除了产品 {" + product + "}且该物品的库存一并删除了");
		// 返回
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}

	/**
	 * 判断该产品名称是否在数据库中已存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(String productName) {
		return baseMapper.selectOne(new QueryWrapper<Product>().eq("product_name", productName)) == null ? false : true;
	}

}
