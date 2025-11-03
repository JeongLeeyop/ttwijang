package com.weilyeat.cms.api.coupon.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.coupon.dto.UserCouponDto;
import com.weilyeat.cms.api.coupon.dto.mapper.UserCouponMapper;
import com.weilyeat.cms.api.coupon.dto.search.UserCouponSearch;
import com.weilyeat.cms.api.coupon.repository.UserCouponRepository;
import com.weilyeat.cms.api.product.dto.ProductOrderDto;
import com.weilyeat.cms.api.product.repository.ProductOrderGroupRepository;
import com.weilyeat.cms.api.product.repository.query.ProductOrderQuery;
import com.weilyeat.cms.api.product.service.ProductOrderGroupService;
import com.weilyeat.cms.common.exception.BadRequestException;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.BadRequest;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.ProductOrderGroup;
import com.weilyeat.cms.entity.UserCoupon;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface UserCouponService {
    Page<UserCouponDto.list> list(SinghaUser authUser, Pageable pageable, UserCouponSearch search);
    List<UserCouponDto.list> usableList(SinghaUser authUser, UserCouponSearch search);
    void download(Integer idx, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class UserCouponServiceImpl implements UserCouponService {
    private final UserCouponRepository userCouponRepository;
    private final ProductOrderGroupRepository orderGroupRepository;
    private final ProductOrderGroupService orderGroupService;
    private final ProductOrderQuery productOrderQuery;

    @Override
    public Page<UserCouponDto.list> list(SinghaUser authUser, Pageable pageable, UserCouponSearch search) {
        search.setUserUid(authUser.getUser().getUid());
        return userCouponRepository.findAll(search.search(), pageable).map(e -> UserCouponMapper.INSTANCE.entityToListDto(e));
    }

    @Override
    public List<UserCouponDto.list> usableList(SinghaUser authUser, UserCouponSearch search) {
        search.setUserUid(authUser.getUser().getUid());
        search.setDownloadStatus(true);
        List<UserCoupon> list = (List) userCouponRepository.findAll(search.search());
        return list.stream().map(e -> UserCouponMapper.INSTANCE.entityToListDto(e)).collect(Collectors.toList());
    }

    @Override
    public void download(Integer idx, SinghaUser authUser) {
        UserCoupon coupon = userCouponRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.COUPON));
        if (!coupon.getUserUid().equals(authUser.getUser().getUid())) throw new BadRequestException(BadRequest.NOT_MINE);
        if (LocalDateTime.now().isAfter(coupon.getExpiredDate())) throw new BadRequestException(BadRequest.EXPIRED_COUPON);

        if(coupon.getType().equals("BUY")){
            ProductOrderGroup order = orderGroupRepository.findById(coupon.getOrderIdx()).orElseThrow(() -> new NotFoundException(NotFound.ORDER));
            
            Integer orderStatus = orderGroupService.findOrderStatus(order.getIdx());
            if (orderStatus == 2) {}
            else {throw new BadRequestException(BadRequest.NOT_PICKUP_ORDER);}
        }

        coupon.setDownloadDate(LocalDateTime.now());
        coupon.setDownloadStatus(true);
        userCouponRepository.save(coupon);
    }
}
