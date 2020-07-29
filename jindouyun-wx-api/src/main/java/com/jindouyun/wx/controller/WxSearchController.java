package com.jindouyun.wx.controller;

import com.github.pagehelper.PageInfo;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.core.validator.Order;
import com.jindouyun.core.validator.Sort;
import com.jindouyun.db.domain.JindouyunCategory;
import com.jindouyun.db.domain.JindouyunGoods;
import com.jindouyun.db.domain.JindouyunSearchHistory;
import com.jindouyun.db.service.JindouyunGoodsService;
import com.jindouyun.db.service.JindouyunSearchHistoryService;
import com.jindouyun.wx.annotation.LoginUser;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WxSearchController
 * @Description 商品搜索服务
 * @Author Bruce
 * @Date 2020/7/29 2:25 下午
 */
@RestController
@RequestMapping("/wx/search")
@CrossOrigin(origins = "*",maxAge = 3600)
public class WxSearchController {

    @Autowired
    private JindouyunSearchHistoryService searchHistoryService;

    @Autowired
    private JindouyunGoodsService goodsService;

    /**
     * 搜索页面信息
     * <p>
     * 如果用户已登录，则给出用户历史搜索记录；
     * 如果没有登录，则给出空历史搜索记录。
     *
     * @param userId 用户ID，可选
     * @return 搜索页面信息
     */
    @GetMapping("index")
    public Object index(@LoginUser Integer userId) {
//        //取出输入框默认的关键词
//        JindouyunKeyword defaultKeyword = keywordsService.queryDefault();
//        //取出热闹关键词
//        List<JindouyunKeyword> hotKeywordList = keywordsService.queryHots();

        List<JindouyunSearchHistory> historyList = null;
        if (userId != null) {
            //取出用户历史关键字
            historyList = searchHistoryService.queryByUid(userId);
        } else {
            historyList = new ArrayList<>(0);
        }

        Map<String, Object> data = new HashMap<String, Object>();
//        data.put("defaultKeyword", defaultKeyword);
        data.put("historyKeywordList", historyList);
//        data.put("hotKeywordList", hotKeywordList);
        return ResponseUtil.ok(data);
    }

    /**
     * 搜素商品
     * <p>
     *
     * @param keyword    关键字，可选
     * @param userId     用户ID
     * @param page       分页页数
     * @param limit      分页大小
     * @param sort       排序方式，支持"add_time", "retail_price"或"name"
     * @param order      排序类型，顺序或者降序
     * @return 根据条件搜素的商品详情
     */
    @GetMapping("list")
    public Object list(
            String keyword,
            @LoginUser Integer userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @Sort(accepts = {"add_time", "retail_price", "name"}) @RequestParam(defaultValue = "add_time") String sort,
            @Order @RequestParam(defaultValue = "desc") String order) {

        //添加到搜索历史
        if (userId != null && !StringUtils.isNullOrEmpty(keyword)) {
            JindouyunSearchHistory searchHistoryVo = new JindouyunSearchHistory();
            searchHistoryVo.setKeyword(keyword);
            searchHistoryVo.setUserId(userId);
            searchHistoryVo.setFrom("wx");
            searchHistoryService.save(searchHistoryVo);
        }

        //查询列表数据
        List<JindouyunGoods> goodsList = goodsService.searchList(keyword, page, limit, sort, order);

        PageInfo<JindouyunGoods> pagedList = PageInfo.of(goodsList);

        Map<String, Object> entity = new HashMap<>();
        entity.put("list", goodsList);
        entity.put("total", pagedList.getTotal());
        entity.put("page", pagedList.getPageNum());
        entity.put("limit", pagedList.getPageSize());
        entity.put("pages", pagedList.getPages());

        // 因为这里需要返回额外的filterCategoryList参数，因此不能方便使用ResponseUtil.okList
        return ResponseUtil.ok(entity);
    }


    /**
     * 清除用户搜索历史
     *
     * @param userId 用户ID
     * @return 清理是否成功
     */
    @PostMapping("clearhistory")
    public Object clearhistory(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        searchHistoryService.deleteByUid(userId);
        return ResponseUtil.ok();
    }
    
}
