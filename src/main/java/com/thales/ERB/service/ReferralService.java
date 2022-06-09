package com.thales.ERB.service;

import java.util.List;
import com.thales.ERB.entity.Referral;
import com.thales.ERB.entity.User;

public interface ReferralService {

	List<Referral> getAllReferrals();
	
	List<Referral> getReferralsByYear(int year);
	
	Referral getReferralById(Long referralId);

	Referral saveReferral(Referral referral, User user);

	Referral updateReferral(Referral referral, User user);

	void deleteReferralbyId(Long referralId, User user);

}
