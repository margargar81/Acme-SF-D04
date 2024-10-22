
package acme.components;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.helpers.MomentHelper;
import acme.client.helpers.RandomHelper;
import acme.client.repositories.AbstractRepository;
import acme.entities.banner.Banner;

@Repository
public interface BannerRepository extends AbstractRepository {

	// Count active banners based on the current date
	@Query("select count(b) from Banner b where :currentDate BETWEEN b.displayPeriodStart AND b.displayPeriodEnd")
	int countBanner(Date currentDate);

	// Retrieve active banners within the specified page request and current date
	@Query("select b from Banner b where :currentDate BETWEEN b.displayPeriodStart AND b.displayPeriodEnd")
	List<Banner> findAllActiveBanners(PageRequest pageRequest, Date currentDate);

	// Fetch a random active banner
	default Banner getActiveRandomBanner() {
		Date now = MomentHelper.getCurrentMoment();
		int count = this.countBanner(now);

		if (count == 0)
			return null;

		int index = RandomHelper.nextInt(0, count);
		PageRequest page = PageRequest.of(index, 1, Sort.by(Direction.ASC, "id"));
		List<Banner> list = this.findAllActiveBanners(page, now);

		return list.isEmpty() ? null : list.get(0);
	}
}
