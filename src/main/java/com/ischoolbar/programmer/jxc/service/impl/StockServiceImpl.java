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
import com.ischoolbar.programmer.jxc.entity.Stock;
import com.ischoolbar.programmer.jxc.mapper.InventoryMapper;
import com.ischoolbar.programmer.jxc.mapper.ProductMapper;
import com.ischoolbar.programmer.jxc.mapper.StockMapper;
import com.ischoolbar.programmer.jxc.pojo.StockVo;
import com.ischoolbar.programmer.jxc.service.IStockService;
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
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

	@Autowired
	InventoryMapper inventoryMapper;
	@Autowired
	LogService logService;

	@Override
	public Page<StockVo> selectByPage(Page<StockVo> page, Stock stock) {
		// TODO Auto-generated method stub
		return baseMapper.selectByPage(page, stock);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> addOrUpdate(Stock stock, HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<>();
		boolean hasId = stock.getStockId() == null ? false : true;
		User user = (User) request.getSession().getAttribute("user");
		if (hasId) {
			// 检查权限，登录的用户是否是该进货的创建者
			Stock oldStock = baseMapper.selectById(stock.getStockId());
			if (!user.getUsername().equals(oldStock.getUsername())) {
				// 如果不是同一个操作者，提示无权限提示用户去列表中修改
				ret.put("type", "error");
				ret.put("msg", "对不起，您不是该数据的创建者，无权更改！");
				return ret;
			}
			// 如果有ID，表示修改，获取旧的进货数量，用新的进货数量-旧的数量，添加到库存的数量，执行修改
			Integer addNum = stock.getStockNum() - oldStock.getStockNum();
			// 更新库存
			Inventory inv = inventoryMapper.selectOne(new QueryWrapper<Inventory>().eq("product_id", stock.getProductId()));
			inv.setInventoryNum(addNum + inv.getInventoryNum());
			inventoryMapper.updateById(inv);
			// 更新进货数量
			baseMapper.updateById(stock);
			ret.put("type", "success");
			ret.put("msg", "修改成功！");
			// 记录日志
			logService.add("用户【" + user.getUsername() + "】修改进货 {" + oldStock + "}为：-->>" + stock);
		} else {
			List<Stock> selectList = baseMapper.selectList(new QueryWrapper<Stock>().eq("product_id", stock.getProductId()).eq("stock_date", stock.getStockDate()));
			// 如果没有ID，表示新增，提示用户去列表中修改，如果没有，执行新增；
			// 条件查看当天是否已经添加了相应的商品;
			if (selectList != null && selectList.size() > 0) {
				// 如果已经存在，提示用户去列表中修改
				ret.put("type", "error");
				ret.put("msg", "进货列表已存在，如需修改，请在列表中双击修改！");
			} else {
				// 如果不存在，执行新增,添加操作人信息
				stock.setUsername(user.getUsername());
				baseMapper.insert(stock);
				// 同步库存数据
				Inventory inv = inventoryMapper.selectOne(new QueryWrapper<Inventory>().eq("product_id", stock.getProductId()));
				inv.setInventoryNum(stock.getStockNum() + inv.getInventoryNum());
				inventoryMapper.updateById(inv);
				ret.put("type", "success");
				ret.put("msg", "添加成功！");
				// 记录日志
				logService.add("用户【" + user.getUsername() + "】添加了进货 {" + stock + "}");
			}
		}
		return ret;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> delete(Integer stockId, HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<>();
		User user = (User) request.getSession().getAttribute("user");
		// 检测权限

		// 删除进货数据的同时，库存中对应的产品数量要减掉删除的数量
		Stock oldStock = baseMapper.selectById(stockId);
		Inventory inv = inventoryMapper.selectOne(new QueryWrapper<Inventory>().eq("product_id", oldStock.getProductId()));
		inv.setInventoryNum(inv.getInventoryNum() - oldStock.getStockNum());
		inventoryMapper.updateById(inv);
		if (baseMapper.deleteById(stockId) < 1) {
			ret.put("type", "error");
			ret.put("msg", "删除库存异常，请联系管理员！");
			return ret;
		}
		// 写入日志
		logService.add("用户【" + user.getUsername() + "】删除了进货 {" + oldStock + "}");
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}

}
