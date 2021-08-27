package top.slomo.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.slomo.miaosha.dao.GoodsDao;
import top.slomo.miaosha.entity.Goods;
import top.slomo.miaosha.entity.MiaoshaGoods;
import top.slomo.miaosha.exception.GlobalException;
import top.slomo.miaosha.result.CodeMsg;
import top.slomo.miaosha.vo.GoodsVo;

import java.util.List;

/**
 * @description: .
 * @date: 2021-04-12
 * @author: YuBo
 */
@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> selectGoodsVoList() {
        return goodsDao.selectGoodsVoList();
    }

    public GoodsVo getGoodsVoById(Long id) {
        return goodsDao.getGoodsVoById(id);
    }

    public void reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setId(goods.getMiaoshaGoodsId());

        int effectLine = goodsDao.reduceStock(g);
        if (effectLine <= 0) {
            throw new GlobalException(CodeMsg.MIAOSHA_FAILED);
        }
    }

    public GoodsVo getGoodsVoByMiaoshaGoodsId(Long miaoshaGoodsId) {
        return goodsDao.getGoodsVoByMiaoshaGoodsId(miaoshaGoodsId);
    }
}
