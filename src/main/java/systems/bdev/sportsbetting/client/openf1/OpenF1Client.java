package systems.bdev.sportsbetting.client.openf1;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import systems.bdev.sportsbetting.domain.openf1.Driver;
import systems.bdev.sportsbetting.domain.openf1.Session;

import java.util.List;

@FeignClient(name = "openF1Client", url = "https://api.openf1.org/v1")
public interface OpenF1Client {

    @GetMapping("/sessions")
    @RateLimiter(name = "openF1ClientSessions")
    List<Session> getSessions(
            @RequestParam(value = "session_type", required = false) String sessionType,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "country_name", required = false) String countryName
    );

    @GetMapping("/drivers")
    @RateLimiter(name = "openF1ClientDrivers")
    List<Driver> getDrivers(
            @RequestParam(value = "session_key", required = false) String sessionKey
    );
}
