package com.thales.ERB.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thales.ERB.ErbApplication;
import com.thales.ERB.entity.Referral;
import com.thales.ERB.entity.User;
import com.thales.ERB.repo.ReferralRepository;
import com.thales.ERB.service.ReferralService;

@Service
public class ReferralServiceImpl implements ReferralService {

	@Autowired
	private ReferralRepository referralRepository;

	private static final Logger logger = LogManager.getLogger(ReferralServiceImpl.class);
	


	@Override
	public List<Referral> getAllReferrals() {
		try {
			logger.debug("ReferralServiceImpl :: getAllReferrals  :: start");
			List<Referral> refList = referralRepository.findAll();
			if(null != refList) {
				logger.debug("ReferralServiceImpl :: getAllReferrals  :: end");
				return refList;
			}
			else {
				logger.debug("ReferralServiceImpl :: getAllReferrals  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("ReferralServiceImpl :: getAllReferrals  :: error occurred" + e.toString());
			return null;
		}
	}

	@Override
	public Referral getReferralById(Long referralId) {
		try {
			logger.debug("ReferralServiceImpl :: getReferralById :: start");
			Referral ref = referralRepository.findById(referralId);
			if(null != ref) {
				logger.debug("ReferralServiceImpl :: getReferralById  :: end");
				return ref;
			}
			else {
				logger.debug("ReferralServiceImpl :: getReferralById  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("ReferralServiceImpl :: getReferralById :: errr occurred" + e.toString());
			return null;
		}
	}

	@Override
	public Referral saveReferral(Referral referral, User user) {
		try {
			logger.debug("ReferralServiceImpl :: saveReferral  :: start");
			Referral ref = referralRepository.save(referral, user);
			if(null != ref) {
				logger.debug("ReferralServiceImpl :: saveReferral  :: end");
				return ref;
			}
			else {
				logger.debug("ReferralServiceImpl :: saveReferral  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("ReferralServiceImpl :: saveReferral :: error occurred" + e.toString());
			return null;
		}
	}

	@Override
	public Referral updateReferral(Referral referral, User user) {
		try {
			logger.debug("ReferralServiceImpl ::updateReferral  :: start");
			Referral ref = referralRepository.update(referral, user);
			if(null != ref) {
				logger.debug("ReferralServiceImpl :: updateReferral  :: end");
				return ref;
			}
			else {
				logger.debug("ReferralServiceImpl :: updateReferral  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("ReferralServiceImpl :: updateReferral  :: error occurred" + e.toString());
			return null;
		}
	}

	@Override
	public void deleteReferralbyId(Long referralId, User user) {
		try {
			logger.debug("ReferralServiceImpl :: deleteReferralbyId  :: start");
			referralRepository.deleteById(referralId, user);
			logger.debug("ReferralServiceImpl :: deleteReferralbyId  :: end");
		} catch (Exception e) {
			logger.debug("ReferralServiceImpl :: deleteReferralbyId  :: error occurred" + e.toString());
		}
	}

	@Override
	public List<Referral> getReferralsByYear(int year) {
		try {
			logger.debug("ReferralServiceImpl :: getAllReferrals  :: start");
			List<Referral> refList = referralRepository.findAllReferralsByYear(year);
			if(null != refList) {
				logger.debug("ReferralServiceImpl :: getAllReferrals  :: end");
				return refList;
			}
			else {
				logger.debug("ReferralServiceImpl :: getAllReferrals  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("ReferralServiceImpl :: getAllReferrals  :: error occurred" + e.toString());
			return null;
		}
	}

}
