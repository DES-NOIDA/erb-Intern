package com.thales.ERB.repo.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.thales.ERB.ErbApplication;
import com.thales.ERB.entity.Constants;
import com.thales.ERB.entity.Employee;
import com.thales.ERB.entity.Points;
import com.thales.ERB.entity.Referral;
import com.thales.ERB.entity.User;
import com.thales.ERB.repo.ReferralRepository;

@Repository
public class ReferralRepositoryImpl implements ReferralRepository {

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = LogManager.getLogger(ReferralRepositoryImpl.class);
	
	public void setSessionfactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	@Override
	public List<Referral> findAll() {
		Session session = null;
		Transaction txn = null;
		try {
			logger.debug("ReferralRepositoryImpl :: findAll  :: start");
			session = this.sessionFactory.openSession();
			txn = session.beginTransaction();
			List<Referral> referrallist = session.createQuery("from Referral").list();
			txn.commit();
			logger.debug("ReferralRepositoryImpl :: findAll  :: end");
			return referrallist;
		}
		catch (Exception e) {
			logger.debug("ReferralRepositoryImpl :: findAll  :: error occurred " + e.toString());
			txn.rollback();
			return null;
		}
		finally {
			session.close();
		}
	}

	@Override
	public Referral findById(Long referralId) {
		Session session = null;
		Transaction txn = null;
		try {
			logger.debug("ReferralRepositoryImpl :: findById :: start");
			session = this.sessionFactory.openSession();
			txn = session.beginTransaction();
			Referral referral = (Referral) session.get(Referral.class, referralId);
			txn.commit();
			logger.debug("ReferralRepositoryImpl :: findById :: end");
			return referral;
		}
		catch (Exception e) {
			logger.debug("ReferralRepositoryImpl :: findById :: error occurred " + e.toString());
			txn.rollback();
			return null;
		}
		finally {
			session.close();
		}
	}

	@Override
	public Referral save(Referral referral, User user) {
		Session session = null;
		Transaction txn = null;
		try {
			logger.debug("ReferralRepositoryImpl :: save  :: start");
			session = this.sessionFactory.openSession();
			txn = session.beginTransaction();
			
			Query query = session.createQuery("from Employee ev where ev.employeeTGI = :employeeTGI");
			query.setCacheable(true);
			query.setParameter("employeeTGI", referral.getReferreeTGI());
			
			Employee emp = (Employee) query.list().get(0);
			emp.setNumberOfReferrals(emp.getNumberOfReferrals() + 1);
			
			referral.setOffer(Constants.YES);
			referral.setOnboarded(Constants.DUE);
			referral.setProbation(Constants.DUE);
			referral.setReferralStatus(Constants.STATUS_ACTIVE);
			referral.setReferralPoints(0l);
			
			// get diversity points from db
			Points diversity1 = (Points) session.get(Points.class, "diversity1"); 
			Points diversity2 = (Points) session.get(Points.class, "diversity2"); 
			Points diversity3 = (Points) session.get(Points.class, "diversity3"); 
			
			// calculate diversity points
			List<Referral> list = emp.getReferral();
			int countDiversity = 0;
			for (Referral ref : list) {
				if (ref.getDiversity().equalsIgnoreCase(Constants.YES)) {
					countDiversity++;
				}
			}
			if (referral.getDiversity().equalsIgnoreCase(Constants.YES)) {
				countDiversity++;
			}
			if (countDiversity % 3 == 1 && referral.getDiversity().equalsIgnoreCase(Constants.YES)) {
				referral.setReferralPoints(referral.getReferralPoints() + Long.valueOf(diversity1.getValue()));
			} else if (countDiversity % 3 == 2 && referral.getDiversity().equalsIgnoreCase(Constants.YES)) {
				referral.setReferralPoints(referral.getReferralPoints() + Long.valueOf(diversity2.getValue()));
			} else if (countDiversity % 3 == 0 && countDiversity != 0  && referral.getDiversity().equalsIgnoreCase(Constants.YES)) {
				referral.setReferralPoints(referral.getReferralPoints() + Long.valueOf(diversity3.getValue()));
			}
			
			emp.setOnboardPoints(emp.getOnboardPoints() + referral.getReferralPoints());
			emp.setTotalPoints(emp.getOnboardPoints() + emp.getExtraPoints());
			
			referral.setEmployee(emp);
			session.saveOrUpdate(referral);
			logger.info(user.getUsername() + " added referral " + referral.getReferralName() + " (" + referral.getCandidateId() + ") to employee " + emp.getEmployeeName() + " (" + emp.getEmployeeTGI() + ")");
			txn.commit();
			logger.debug("ReferralRepositoryImpl :: save  :: end");
			return referral;
		}
		catch (Exception e) {
			logger.debug("ReferralRepositoryImpl :: save  :: error occurred " + e.toString());
			txn.rollback();
			return null;
		}
		finally {
			session.close();
		}
	}

	@Override
	public Referral update(Referral referral, User user) {
		Session session = null;
		Transaction txn = null;
		try {
			logger.debug("ReferralRepositoryImpl :: update  :: start");
			session = this.sessionFactory.openSession();
			txn = session.beginTransaction();
			
			Query query = session.createQuery("from Employee ev where ev.employeeTGI = :employeeTGI");
			query.setCacheable(true);
			query.setParameter("employeeTGI", referral.getReferreeTGI());
			
			Employee emp = (Employee) query.list().get(0);
			referral.setOffer(Constants.YES);
			
			// get points from db
			Points onboarded = (Points) session.get(Points.class, "onboarded"); 
			Points probation_completed = (Points) session.get(Points.class, "probation_completed"); 
			Points not_onboarded = (Points) session.get(Points.class, "not_onboarded"); 
			Points probation_not_completed = (Points) session.get(Points.class, "probation_not_completed");
			Points successful = (Points) session.get(Points.class, "successful"); 
			Points diversity1 = (Points) session.get(Points.class, "diversity1"); 
			Points diversity2 = (Points) session.get(Points.class, "diversity2"); 
			Points diversity3 = (Points) session.get(Points.class, "diversity3"); 
			Points unsuccessful = (Points) session.get(Points.class, "unsuccessful"); 
			
			if (referral.getReferralStatus().equalsIgnoreCase(Constants.STATUS_ACTIVE)) {
				if (referral.getOnboarded().equalsIgnoreCase(Constants.NO)) {
					referral.setProbation(Constants.NO);
					referral.setReferralPoints(referral.getReferralPoints() + Long.valueOf(not_onboarded.getValue()));
					emp.setOnboardPoints(emp.getOnboardPoints() + Long.valueOf(not_onboarded.getValue()));
					referral.setReferralStatus(Constants.STATUS_NOT_ONBOARDED);
				} else if (referral.getOnboarded().equalsIgnoreCase(Constants.YES)) {
					referral.setReferralPoints(referral.getReferralPoints() + Long.valueOf(onboarded.getValue()));
					emp.setOnboardPoints(emp.getOnboardPoints() + Long.valueOf(onboarded.getValue()));
					referral.setReferralStatus(Constants.STATUS_ONBOARDED);
				}
			}
			if (referral.getReferralStatus().equalsIgnoreCase(Constants.STATUS_ONBOARDED)) {
				if (referral.getProbation().equalsIgnoreCase(Constants.YES)) {
					referral.setReferralPoints(referral.getReferralPoints() + Long.valueOf(probation_completed.getValue()));
					emp.setOnboardPoints(emp.getOnboardPoints() + Long.valueOf(probation_completed.getValue()));
					referral.setReferralStatus(Constants.STATUS_PROBATION_COMPLETED);
				} else if (referral.getProbation().equalsIgnoreCase(Constants.NO)) {
					referral.setReferralPoints(
							referral.getReferralPoints() + Long.valueOf(probation_not_completed.getValue()));
					emp.setOnboardPoints(emp.getOnboardPoints() + Long.valueOf(probation_not_completed.getValue()));
					referral.setReferralStatus(Constants.STATUS_PROBATION_NOT_COMPLETED);
				}
			}

			// 3 referrals bonus points
			List<Referral> referralList = emp.getReferral();
			int probationYes = 0, probationNo = 0;
			for (Referral ref : referralList) {
				if (ref.getProbation().equalsIgnoreCase(Constants.YES) && !ref.getReferralStatus().equalsIgnoreCase(Constants.STATUS_INACTIVE)) {
					probationYes++;
				} else if (ref.getProbation().equalsIgnoreCase(Constants.NO) && !ref.getReferralStatus().equalsIgnoreCase(Constants.STATUS_INACTIVE)) {
					probationNo++;
				}
			}
			if (referral.getProbation().equalsIgnoreCase(Constants.YES)
					&& !referral.getReferralStatus().equalsIgnoreCase(Constants.STATUS_INACTIVE)) {
				probationYes++;
			} else if (referral.getProbation().equalsIgnoreCase(Constants.NO)
					&& !referral.getReferralStatus().equalsIgnoreCase(Constants.STATUS_INACTIVE)) {
				probationNo++;
			}
			if (probationNo == 3) {
				emp.setExtraPoints(emp.getExtraPoints() + Long.valueOf(unsuccessful.getValue()));
				for (Referral ref : referralList) {
					if (ref.getReferralStatus().equalsIgnoreCase(Constants.STATUS_PROBATION_NOT_COMPLETED) || ref.getReferralStatus().equalsIgnoreCase(Constants.STATUS_NOT_ONBOARDED)) {
						ref.setReferralStatus(Constants.STATUS_INACTIVE);
					}
				}
				if (referral.getReferralStatus().equalsIgnoreCase(Constants.STATUS_PROBATION_NOT_COMPLETED) || referral.getReferralStatus().equalsIgnoreCase(Constants.STATUS_NOT_ONBOARDED) ) {
					referral.setReferralStatus(Constants.STATUS_INACTIVE);
				}
			}
			if (probationYes == 3) {
				emp.setExtraPoints(emp.getExtraPoints() + Long.valueOf(successful.getValue()));
				for (Referral ref : referralList) {
					if (ref.getReferralStatus().equalsIgnoreCase(Constants.STATUS_PROBATION_COMPLETED)) {
						ref.setReferralStatus(Constants.STATUS_INACTIVE);
					}
				}
				if (referral.getReferralStatus().equalsIgnoreCase(Constants.STATUS_PROBATION_COMPLETED)) {
					referral.setReferralStatus(Constants.STATUS_INACTIVE);
				}
			}
			emp.setTotalPoints(emp.getOnboardPoints() + emp.getExtraPoints());
			referral.setEmployee(emp);
			Referral ref = (Referral) session.merge(referral);
			logger.info(user.getUsername() + " updated referral " + referral.getReferralName() + " (" + referral.getCandidateId() + ") for employee " + emp.getEmployeeName() + " (" + emp.getEmployeeTGI() + ")");
			if(null == ref) {
				return ref;
			}
			txn.commit();
			logger.debug("ReferralRepositoryImpl :: update  :: end");
			return referral;
		}
		catch (Exception e) {
			logger.debug("ReferralRepositoryImpl :: update  :: error occurred" + e.toString());
			txn.rollback();
			referral = null;
			return referral;
		}
		finally {
			session.close();
		}
	}

	@Override
	public void deleteById(Long referralId, User user) {
		Session session = null;
		Transaction txn = null;
		try {
			logger.debug("ReferralRepositoryImpl :: deleteById  :: start");
			session = this.sessionFactory.openSession();
			txn = session.beginTransaction();
			Referral referral = (Referral) session.load(Referral.class, new Long(referralId));
			Employee employee = referral.getEmployee();
			Points successful = (Points) session.get(Points.class, "successful"); 
			Points unsuccessful = (Points) session.get(Points.class, "unsuccessful"); 
			if (null != referral) {
				employee.setNumberOfReferrals(employee.getNumberOfReferrals() - 1);
				employee.setOnboardPoints(employee.getOnboardPoints() - referral.getReferralPoints());
				employee.setExtraPoints(0l);
				List<Referral> referralList = employee.getReferral();
				int probationYes = 0, probationNo = 0;
				for (Referral ref : referralList) {
					if (!ref.getReferreeTGI().equalsIgnoreCase(referral.getReferreeTGI())) {
						if (ref.getProbation().equalsIgnoreCase(Constants.YES)) {
							ref.setReferralStatus(Constants.STATUS_PROBATION_COMPLETED);
							probationYes++;
						} else if (ref.getProbation().equalsIgnoreCase(Constants.NO)) {
							ref.setReferralStatus(Constants.STATUS_PROBATION_NOT_COMPLETED);
							probationNo++;
						}
					}
				}
				if (probationNo % 3 == 0 && probationNo != 0) {
					employee.setExtraPoints(employee.getExtraPoints() + Long.valueOf(unsuccessful.getValue()));
					for (Referral r : referralList) {
						if (r.getReferralStatus().equalsIgnoreCase(Constants.STATUS_PROBATION_NOT_COMPLETED)) {
							r.setReferralStatus(Constants.STATUS_INACTIVE);
						}
					}
				}
				if (probationYes % 3 == 0 && probationYes != 0) {
					employee.setExtraPoints(employee.getExtraPoints() + Long.valueOf(successful.getValue()));
					for (Referral r : referralList) {
						if (r.getReferralStatus().equalsIgnoreCase(Constants.STATUS_PROBATION_COMPLETED)) {
							r.setReferralStatus(Constants.STATUS_INACTIVE);
						}
					}
				}
				employee.setTotalPoints(employee.getOnboardPoints() + employee.getExtraPoints());
				referral.setEmployee(employee);
				session.update(referral);
				session.delete(referral);
				logger.info(user.getUsername() + " deleted referral " + referral.getReferralName() + " (" + referral.getCandidateId() + ") in employee " + employee.getEmployeeName() + " (" + employee.getEmployeeTGI() + ")");
			}
			txn.commit();
			logger.debug("ReferralRepositoryImpl :: deleteById  :: end");
		}
		catch (Exception e) {
			logger.debug("ReferralRepositoryImpl :: deleteById  :: error occurred" + e.toString());
			txn.rollback();
		}
		finally {
			session.close();
		}
	}

	@Override
	public List<Referral> findAllReferralsByYear(int year) {
		Session session = null;
		Transaction txn = null;
		try {
			logger.debug("ReferralRepositoryImpl :: findAll  :: start");
			session = this.sessionFactory.openSession();
			txn = session.beginTransaction();
			List<Referral> referrallist = session.createQuery("from Referral r where year(dateOfJoining)=" + year).list();
			txn.commit();
			logger.debug("ReferralRepositoryImpl :: findAll  :: end");
			return referrallist;
		}
		catch (Exception e) {
			logger.debug("ReferralRepositoryImpl :: findAll  :: error occurred " + e.toString());
			txn.rollback();
			return null;
		}
		finally {
			session.close();
		}
	}

}
