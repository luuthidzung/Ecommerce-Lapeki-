package com.ecommerce.service.impl;

import com.ecommerce.dao.VoucherDAO;
import com.ecommerce.dao.impl.VoucherDAOImpl;
import com.ecommerce.model.Voucher;
import com.ecommerce.service.VoucherService;

import java.util.List;

/**
 * @overview VoucherServiceImpl implements all methods of VoucherService
 * interface.
 */
public class VoucherServiceImpl implements VoucherService {
    VoucherDAO voucherDAO = new VoucherDAOImpl();

    /**
     * Insert a new voucher
     *
     * @param voucher - the voucher to be inserted to database
     */
    public void insertVoucher(Voucher voucher) {
        voucherDAO.insertVoucher(voucher);
    }

    /**
     * Update an existing voucher
     *
     * @param voucher - the voucher to be updated
     */
    public void updateVoucher(Voucher voucher) {
        voucherDAO.updateVoucher(voucher);
    }

    /**
     * Delete an existing voucher
     *
     * @param voucherID - the id of the voucher to be deleted
     */
    public void deleteVoucher(int voucherID) {
        voucherDAO.deleteVoucher(voucherID);
    }

    /**
     * Retrieve a voucher by its id
     *
     * @param voucherID - the id of the desired voucher
     */
    public Voucher getVoucherByID(int voucherID) {
        return voucherDAO.getVoucherByID(voucherID);
    }

    /**
     * Retrieve all voucher
     *
     * @return Either the list of all vouchers or null if there is none
     */
    public List<Voucher> getAllVouchers() {
        return voucherDAO.getAllVouchers();
    }

}