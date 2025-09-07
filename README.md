# sportsbetting

## How to start
from the repo root:
./gradlew bootRun

## List Events
```
curl -G "http://localhost:8095/api/event/list" \
--data-urlencode "sessionType=Race" \
--data-urlencode "year=2023" \
--data-urlencode "country=Hungary"
```


## Make a bet
```
curl -X POST "http://localhost:8095/api/bet/create" \
--data-urlencode  "userId=1234" \
--data-urlencode  "amount=95.50" \
--data-urlencode  "eventId=OpenF1,9133" \
--data-urlencode  "selectionId=OpenF1,Leclerc16"
```

## Finalize event with selection
```
curl -X POST "http://localhost:8095/api/outcome/finalize" \
--data-urlencode   "eventId=OpenF1,9133" \
--data-urlencode   "selectionId=OpenF1,Leclerc16"
```
