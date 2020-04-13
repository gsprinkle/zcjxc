package com.ischoolbar.programmer.jxc.service.impl;

import com.ischoolbar.programmer.jxc.entity.Stock;
import com.ischoolbar.programmer.jxc.mapper.StockMapper;
import com.ischoolbar.programmer.jxc.service.IStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-03-30
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

}
