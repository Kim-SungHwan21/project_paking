package com.nhnacademy.project.parking;

public class PaymentService {
    private DiscountPolicyRepository repository;

    public PaymentService(DiscountPolicyRepository repository) {
        this.repository = repository;
    }

    /**
     * 실시간 할인내역 조회
     *
     * @param productAmt   상품금액
     * @param discountCode 할인코드 (PAYCO:페이코-10%, DAANNAWA:다안나와검색-15% FANCAFE:팬카페-1000원)
     * @return 할인내역
     */
    public Discount getDiscount(long productAmt, String discountCode) {
        // 할인금액
        long discountAmt = getDiscountAmt(productAmt, discountCode);
        return Discount.of(discountAmt);
    }

    public Discount getDiscount(long productAmt, String... codes) {
        // 할인금액
        long discountAmt = getDiscountAmt(productAmt, codes);
        return Discount.of(discountAmt);
    }

    /**
     * 결재 처리
     *
     * @param productAmt   상품금액
     * @param discountCode 할인코드 (PAYCO:페이코-10%, DAANNAWA:다안나와검색-15% FANCAFE:팬카페-1000원)
     */
    public void payment(Customer customer, long productAmt, String discountCode) {
        // 결제금액
        long paymentAmt = productAmt - getDiscountAmt(productAmt, discountCode);
    }

    private long getDiscountAmt(long productAmt, String... discountCodes) {

            if (discountCodes.length == 1) {
                return repository.findByCode(discountCodes[0]).getDiscountAmt(productAmt);
            }
        return new CompositeDiscounter(repository, discountCodes).getDiscountAmt(productAmt);
    }
}
