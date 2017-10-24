# Twitter Ads Stats parser plugin for Embulk

[Twitter Ads Stats](https://developer.twitter.com/en/docs/ads/analytics/overview/metrics-and-segmentation)

## Overview

* **Plugin type**: parser
* **Guess supported**: no

## Configuration

- **stop_on_invalid_record**: Stop bulk load transaction if a file includes invalid record (such as invalid timestamp) (boolean, default: false)

## Example

#### Basic Usage

```
{
  "data": [
    {
      "id": "xxxxx",
      "id_data": [
        {
          "metrics": {
            "a": [
              510,
              494,
              364
            ],
            "b": null,
            "c": {
              "e": null,
              "f": null
            },
            "d": {
              "e": [
                2,
                3,
                4
              ],
              "f": [
                12,
                19,
                13
              ]
            }
          }
        }
      ]
    }
  ],
  "data_type": "stats",
  "request": {
    "params": {
      "country": null,
      "end_time": "2017-08-28T15:00:00Z",
      "entity": "CAMPAIGN",
      "entity_ids": [
        "xxxxx"
      ],
      "granularity": "DAY",
      "metric_groups": [
        "BILLING",
        "ENGAGEMENT",
        "LIFE_TIME_VALUE_MOBILE_CONVERSION",
        "MEDIA",
        "MOBILE_CONVERSION",
        "VIDEO",
        "WEB_CONVERSION"
      ],
      "placement": "ALL_ON_TWITTER",
      "platform": null,
      "segmentation_type": null,
      "start_time": "2017-08-20T15:00:00Z"
    }
  },
  "time_series_length": 8
}
```

```yaml
in:
  type: any file input plugin type
  parser:
    type: sample
    stop_on_invalid_record: true
```

"time_series_length" determines the length of the array of "metrics".
If there is no key in the order of "time_series_length", it is judged as invalid record

## Build

```
$ ./gradlew gem  # -t to watch change of files and rebuild continuously
```

## Test

```
sbt test
```

## Acknowledgement

I developed this library with reference to [embulk-parser-firebase_avro](https://github.com/smdmts/embulk-parser-firebase_avro)
Thank you very much.
