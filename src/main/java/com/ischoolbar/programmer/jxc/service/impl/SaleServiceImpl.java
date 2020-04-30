package com.ischoolbar.programmer.jxc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ischoolbar.programmer.entity.admin.User;
import com.ischoolbar.programmer.jxc.entity.Inventory;
import com.ischoolbar.programmer.jxc.entity.Sale;
import com.ischoolbar.programmer.jxc.mapper.InventoryMapper;
import com.ischoolbar.programmer.jxc.mapper.SaleMapper;
import com.ischoolbar.programmer.jxc.pojo.SaleVo;
import com.ischoolbar.programmer.jxc.service.ISaleService;
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
public class SaleServiceImpl extends ServiceImpl<SaleMapper, Sale> implements ISaleService {
	@Autowired
	InventoryMapper inventoryMapper;

	@Autowired
	LogService logService;

	@Transactional
	@Override
	public Map<String, Object> addOrUpdate(Sale sale, HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<>();
		boolean hasId = sale.getSaleId() == null ? false : true;
		User user = (User) request.getSession().getAttribute("user");
		if (hasId) {
			// 如果有ID,表示为修改，检查用户，是否有权限，无权限直接返回
			Sale oldSale = baseMapper.selectById(sale.getSaleId());
			// 如果不是同一个操作者，提示无权限
			if (!user.getUsername().equals(oldSale.getUsername())) {
				ret.put("type", "error");
				ret.put("msg", "对不起，您不是该数据的创建者，无权更改！");
				return ret;
			}
			// 获取旧的销售数量，用新的销售数量-旧的数量，用库存的数量减掉该数，执行修改
			Integer minusNum = sale.getSaleNum() - oldSale.getSaleNum();
			// 更新库存
			Inventory inv = inventoryMapper.selectOne(new QueryWrapper<Inventory>().eq("product_id", sale.getProductId()));
			inv.setInventoryNum(inv.getInventoryNum() - minusNum);
			inventoryMapper.updateById(inv);
			// 更新销售
			baseMapper.updateById(sale);
			// 记录日志
			logService.add("用户【" + user.getUsername() + "】修改销售数据 {" + oldSale + "}为：-->>" + sale);
			ret.put("type", "success");
			ret.put("msg", "修改成功！");
			
		} else { // 如果没有ID，表示新增
			List<Sale> selectList = baseMapper.selectList(new QueryWrapper<Sale>().eq("product_id", sale.getProductId()).eq("Sale_date", sale.getSaleDate()));
			// 如果没有ID，表示新增，提示用户去列表中修改，如果没有，执行新增；
			// 条件查看当天是否已经添加了相应的商品;
			if (selectList != null && selectList.size() > 0) {
				// 如果已经存在，提示用户去列表中修改
				ret.put("type", "error");
				ret.put("msg", "当天的销售列表已存在，无需再次添加！");
				return ret;
			} else {
				// 如果不存在，设置操作者标识
				sale.setUsername(user.getUsername());
				// 执行新增
				baseMapper.insert(sale);
				// 记录日志
				logService.add("用户【" + user.getUsername() + "】添加了销售数据 {" + sale + "}");
				// 同步库存数据
				Inventory inv = inventoryMapper.selectOne(new QueryWrapper<Inventory>().eq("product_id", sale.getProductId()));
				// 新的库存 = 原有库存-销售量
				inv.setInventoryNum(inv.getInventoryNum() - sale.getSaleNum());
				inventoryMapper.updateById(inv);
				ret.put("type", "success");
				ret.put("msg", "添加成功！");
			}
		}
		return ret;
	}

	@Transactional
	@Override
	public Map<String, Object> delete(Integer saleId, HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<>();
		// 删除进货数据的同时，库存中对应的产品数量要增加删除的数量
		Sale oldsale = baseMapper.selectById(saleId);
		Inventory inv = inventoryMapper.selectOne(new QueryWrapper<Inventory>().eq("product_id", oldsale.getProductId()));
		inv.setInventoryNum(inv.getInventoryNum() + oldsale.getSaleNum());
		inventoryMapper.updateById(inv);
		if (baseMapper.deleteById(saleId) < 1) {
			ret.put("type", "error");
			ret.put("msg", "删除库存异常，请联系管理员！");
			return ret;
		}
		// 记录日志：先获取登录的用户
		User user = (User) request.getSession().getAttribute("user");
		// 写入日志
		logService.add("用户【" + user.getUsername() + "】删除了销售数据 {" + oldsale + "}");
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}

	@Override
	public Page<SaleVo> selectByPage(Page<SaleVo> page, Sale sale) {
		return baseMapper.selectByPage(page, sale);
	}

}
