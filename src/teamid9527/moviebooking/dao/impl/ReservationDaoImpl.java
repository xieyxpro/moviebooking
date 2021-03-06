package teamid9527.moviebooking.dao.impl;

import java.util.List;

import net.bytebuddy.utility.privilege.GetSystemPropertyAction;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import teamid9527.moviebooking.dao.ReservationDao;
import teamid9527.moviebooking.entities.Customer;
import teamid9527.moviebooking.entities.MovieItem;
import teamid9527.moviebooking.entities.Reservation;

@Repository
public class ReservationDaoImpl implements ReservationDao {

	@Autowired
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public Reservation findReservationById(Integer id) {
		Reservation reservation = getSession().get(Reservation.class, id);
		return reservation;
	}

	public Reservation findReservationByCustomerId(Integer customer_id) {
		Criteria criteria = getSession().createCriteria(Reservation.class);
		criteria.add(Restrictions.eq("customer.id", customer_id));
		return (Reservation)criteria.uniqueResult();
	}

	public Reservation findReservationByCustomer(Customer customer) {
		return findReservationByCustomerId(customer.getC_id());
	}

	public void createReservation(Reservation reservation) {
		Reservation reservation2 = findReservationByCustomer(reservation.getCustomer());
		if (reservation2 == null)
			getSession().save(reservation);
		else {
			reservation2.setMovieItems(reservation.getMovieItems());
			getSession().update(reservation2);
		}
	}

	public void updateReservation(Reservation reservation) {
		getSession().evict(getSession().get(Reservation.class, reservation.getId()));
		getSession().update(reservation);
	}

	public void deleteReservationById(Integer id) {
		Reservation reservation = (Reservation)getSession().get(Reservation.class, id);
		if (reservation != null)
			getSession().delete(reservation);
	}
	
}
