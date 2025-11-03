package com.weilyeat.cms.api.settle.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.product.repository.ProductOrderRepository;
import com.weilyeat.cms.api.product.service.AdmProductOrderService;
import com.weilyeat.cms.api.settle.dto.ShopAdmSettleApplyDto;
import com.weilyeat.cms.api.settle.dto.mapper.ShopAdmSettleApplyMapper;
import com.weilyeat.cms.api.settle.dto.search.ShopAdmSettleApplySearch;
import com.weilyeat.cms.api.settle.repository.SettleApplyRepository;
import com.weilyeat.cms.api.settle.repository.SettleSettingRepository;
import com.weilyeat.cms.api.settle.repository.query.SettleApplyItemQuery;
import com.weilyeat.cms.api.shop.repository.ShopRepository;
import com.weilyeat.cms.common.exception.BadRequestException;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.BadRequest;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.ProductOrder;
import com.weilyeat.cms.entity.SettleApply;
import com.weilyeat.cms.entity.SettleApplyItem;
import com.weilyeat.cms.entity.SettleSetting;
import com.weilyeat.cms.entity.Shop;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface ShopAdmSettleApplyService {
    Page<ShopAdmSettleApplyDto.list> list(Pageable pageable, SinghaUser authUser);
    ShopAdmSettleApplyDto.detail detail(Integer idx, SinghaUser authUser);
    void apply(ShopAdmSettleApplyDto.apply dto, SinghaUser authUser);
    void cancel(Integer idx, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class ShopAdmSettleApplyServiceImpl implements ShopAdmSettleApplyService {
    private final SettleApplyRepository settleApplyRepository;
    private final ShopRepository shopRepository;
    private final ProductOrderRepository productOrderRepository;
    private final SettleSettingRepository settleSettingRepository;
    private final SettleApplyItemQuery settleApplyItemQuery;

    @Override
    public Page<ShopAdmSettleApplyDto.list> list(Pageable pageable, SinghaUser authUser) {
        ShopAdmSettleApplySearch search = new ShopAdmSettleApplySearch();
        search.setShopIdx(authUser.getUser().getShopIdx());
        return settleApplyRepository.findAll(search.search(), pageable).map(e -> ShopAdmSettleApplyMapper.INSTANCE.entityToListDto(e));
    }

    @Override
    public ShopAdmSettleApplyDto.detail detail(Integer idx, SinghaUser authUser) {
        SettleApply entity = settleApplyRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SETTLE_APPLY));
        if (entity.getShopIdx() != authUser.getUser().getShopIdx()) throw new BadRequestException(BadRequest.NOT_MINE);
        
        ShopAdmSettleApplyDto.detail data = ShopAdmSettleApplyMapper.INSTANCE.entityToDetailDto(entity);
        data.setItems(settleApplyItemQuery.listByShopAdmin(idx));
        return data;
    }

    @Override
    public void apply(ShopAdmSettleApplyDto.apply dto, SinghaUser authUser) {
        User user = authUser.getUser();
        Shop shop = shopRepository.findById(user.getShopIdx()).orElseThrow(() -> new NotFoundException(NotFound.SHOP));

        int totalSale = 0; // 통합 판매액
        int totalProductNum = 0; // 통합 판매개수
        List<ProductOrder> orderList = productOrderRepository.findByDayIdxAndIdxIn(dto.getDayIdxList());
        List<SettleApplyItem> items = new ArrayList<SettleApplyItem>();
        for (ProductOrder order : orderList) {
            if (order.getPickupStatus() != 1) throw new BadRequestException(BadRequest.NOT_PICKUP_ORDER);
            if (order.getSettleStatus() != 0) throw new BadRequestException(BadRequest.ALREADY_APPLY_SETTLE);
            totalSale += order.getAmount();
            totalProductNum += order.getProductNum();

            SettleApplyItem item = new SettleApplyItem();
            item.setOrderIdx(order.getIdx());
            items.add(item);

            order.setSettleStatus(1);
        }

        SettleSetting settleSetting = settleSettingRepository.findTopByOrderByIdxDesc().orElseThrow(() -> new BadRequestException(BadRequest.NOT_SET_SETTLE_AMOUNT));
        
        SettleApply entity = new SettleApply(shop, totalSale, dto.getDayIdxList().size() * settleSetting.getAmount(), totalProductNum,user);
        for (SettleApplyItem item : items) item.setApply(entity);
        entity.setItems(items);

        productOrderRepository.saveAll(orderList);
        settleApplyRepository.save(entity);
    }

    @Override
    public void cancel(Integer idx, SinghaUser authUser) {
        SettleApply entity = settleApplyRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SETTLE_APPLY));
        if (entity.getShopIdx() != authUser.getUser().getShopIdx()) throw new BadRequestException(BadRequest.NOT_MINE);
        if (entity.isApprovalStatus()) throw new BadRequestException(BadRequest.ALREADY_APPROVAL_SETTLE_APPLY);
        entity.setCancelStatus(true);

        settleApplyRepository.save(entity);
    }
}
