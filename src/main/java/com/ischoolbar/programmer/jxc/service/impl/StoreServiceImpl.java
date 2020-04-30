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
import com.ischoolbar.programmer.jxc.entity.Employee;
import com.ischoolbar.programmer.jxc.entity.Inventory;
import com.ischoolbar.programmer.jxc.entity.Store;
import com.ischoolbar.programmer.jxc.mapper.InventoryMapper;
import com.ischoolbar.programmer.jxc.mapper.StoreMapper;
import com.ischoolbar.programmer.jxc.service.IStoreService;
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
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements IStoreService {
	@Autowired
	LogService logService;
	@Autowired
	InventoryMapper inventoryMapper;
	

	@Override
	public Page<Store> selectByPage(Page<Store> page, Store store) {
		QueryWrapper<Store> queryWrapper = new QueryWrapper<>(store);
		return baseMapper.selectPage(page, queryWrapper);
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> addOrUpdate(Store store, HttpServletRequest request) {
		boolean hasId = store.getStoreId() == null ? false : true;
		Map<String, Object> ret = new HashMap<>();
		// 数据校验
		if (StringUtils.isEmpty(store.getStoreName())) {
			ret.put("type", "error");
			ret.put("msg", "仓库名不能为空");
			return ret;
		}
		if (!hasId && isExist(store.getStoreName())) {
			ret.put("type", "error");
			ret.put("msg", "该仓库已经存在，请重新输入！");
			return ret;
		}
		// 获取登录信息
		User user = (User) request.getSession().getAttribute("user");
		// 如果没有ID，表示新增
		if (!hasId) {
			if(baseMapper.insert(store) > 0){
				// 记录日志
				logService.add("用户【" + user.getUsername() + "】添加了仓库 {" + store + "}");
			}else{
				ret.put("type", "error");
				ret.put("msg", "新增仓库异常，请联系管理员！");
				return ret;
			}
		}else{// 有ID，表示修改，记录日志
			Store oldStore = baseMapper.selectById(store.getStoreId());
			if(baseMapper.updateById(store) > 0){
				store = baseMapper.selectById(store.getStoreId());
				logService.add("用户【" + user.getUsername() + "】修改仓库 {" + oldStore + "}为：-->>" + store);
			}else{
				ret.put("type", "error");
				ret.put("msg", "修改仓库异常，请联系管理员！");
				return ret;
			}
		}
		ret.put("type", "success");
		ret.put("msg", hasId ? "仓库修改成功！" : "仓库添加成功！");
		return ret;
	}

	/**
	 * 判断该仓库名称是否在数据库中已存在
	 * 
	 * @param cName
	 * @return
	 */
	private boolean isExist(String storeName) {
		return baseMapper.selectOne(new QueryWrapper<Store>().eq("store_name", storeName)) == null ? false : true;
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> delete(Integer storeId, HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<>();
		// 查看是否可以删除,该仓库中是否有产品
		List<Inventory> invList = inventoryMapper.selectList(new QueryWrapper<Inventory>().eq("store_id", storeId));
		if(invList != null && invList.size() > 0){
			ret.put("type", "error");
			ret.put("msg", "删除仓库异常，该仓库保存有产品，无法删除！");
			return ret;
		}
		// 查询要删除的对象，方便日志记录
		Store store = baseMapper.selectById(storeId);
		// 正常删除，记录日志
		// 记录日志：先获取登录的用户
		User user = (User) request.getSession().getAttribute("user");
		if (baseMapper.deleteById(storeId) > 0) {
			// 写入日志
			logService.add("用户【" + user.getUsername() + "】删除了库存 {" + store + "}");
		}else{
			ret.put("type", "error");
			ret.put("msg", "删除仓库异常，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}

}
