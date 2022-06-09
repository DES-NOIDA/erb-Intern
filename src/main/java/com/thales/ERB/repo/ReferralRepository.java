package com.thales.ERB.repo;

import java.util.List;
import com.thales.ERB.entity.Referral;
import com.thales.ERB.entity.User;

public interface ReferralRepository {

	public List<Referral> findAll();
	
	public List<Referral> findAllReferralsByYear(int year);

	public Referral findById(Long referralId);

	public Referral save(Referral referral, User user);
	
	public Referral update(Referral referral, User user);

	public void deleteById(Long referralId, User user);

}