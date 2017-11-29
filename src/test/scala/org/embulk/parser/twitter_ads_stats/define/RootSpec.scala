package org.embulk.parser.twitter_ads_stats.define

import java.time.{LocalDate, LocalDateTime}

import org.embulk.parser.twitter_ads_stats.{Column, InvalidMetricTimeSeriesException, MetricElementNames, UnitSpec}
import org.scalatest.EitherValues._

class RootSpec extends UnitSpec {

  "メトリクス要素名から複数のカラムを解決する" in {
    import RootSpec._

    val actual = defaultRoot.resolveColumns(metricElementNames)

    val expected = List(
      Column(
        "123",
        LocalDate.of(2017, 1, 1),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views"       -> Some(1),
            "media_engagements" -> None
          ),
          "billing" -> Map(
            "billed_engagements"        -> Some(1),
            "billed_charge_local_micro" -> Some(1)
          ),
          "web_conversion" -> Map(
            "conversion_purchases_assisted" -> Some(1)
          )
        )
      ),
      Column(
        "123",
        LocalDate.of(2017, 1, 2),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views"       -> Some(2),
            "media_engagements" -> None
          ),
          "billing" -> Map(
            "billed_engagements"        -> Some(2),
            "billed_charge_local_micro" -> Some(2)
          ),
          "web_conversion" -> Map(
            "conversion_purchases_assisted" -> Some(2)
          )
        )
      ),
      Column(
        "123",
        LocalDate.of(2017, 1, 1),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views"       -> Some(10),
            "media_engagements" -> None
          ),
          "billing" -> Map(
            "billed_engagements"        -> Some(10),
            "billed_charge_local_micro" -> Some(10)
          ),
          "web_conversion" -> Map(
            "conversion_purchases_assisted" -> Some(10)
          )
        )
      ),
      Column(
        "123",
        LocalDate.of(2017, 1, 2),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views"       -> Some(20),
            "media_engagements" -> None
          ),
          "billing" -> Map(
            "billed_engagements"        -> Some(20),
            "billed_charge_local_micro" -> Some(20)
          ),
          "web_conversion" -> Map(
            "conversion_purchases_assisted" -> Some(20)
          )
        )
      ),
      Column(
        "456",
        LocalDate.of(2017, 1, 1),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views"       -> Some(1),
            "media_engagements" -> None
          ),
          "billing" -> Map(
            "billed_engagements"        -> Some(1),
            "billed_charge_local_micro" -> Some(1)
          ),
          "web_conversion" -> Map(
            "conversion_purchases_assisted" -> Some(1)
          )
        )
      ),
      Column(
        "456",
        LocalDate.of(2017, 1, 2),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views"       -> Some(2),
            "media_engagements" -> None
          ),
          "billing" -> Map(
            "billed_engagements"        -> Some(2),
            "billed_charge_local_micro" -> Some(2)
          ),
          "web_conversion" -> Map(
            "conversion_purchases_assisted" -> Some(2)
          )
        )
      ),
      Column(
        "456",
        LocalDate.of(2017, 1, 1),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views"       -> Some(10),
            "media_engagements" -> None
          ),
          "billing" -> Map(
            "billed_engagements"        -> Some(10),
            "billed_charge_local_micro" -> Some(10)
          ),
          "web_conversion" -> Map(
            "conversion_purchases_assisted" -> Some(10)
          )
        )
      ),
      Column(
        "456",
        LocalDate.of(2017, 1, 2),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views"       -> Some(20),
            "media_engagements" -> None
          ),
          "billing" -> Map(
            "billed_engagements"        -> Some(20),
            "billed_charge_local_micro" -> Some(20)
          ),
          "web_conversion" -> Map(
            "conversion_purchases_assisted" -> Some(20)
          )
        )
      )
    )
    val rightValue = actual.right.value
    assert(rightValue == expected)
  }
  "メトリクス時系列のコレクション要素数以上の期間が指定された時、 InvalidMetricTimeSeriesException となる" in {
    import RootSpec._

    val actual = createRoot(
      Request(
        params = Params(
          start_time = StatsDateTime("2017-01-01T01:01:01Z"),
          end_time = StatsDateTime("2017-01-04T01:01:01Z"),
          placement = ""
        )
      )
    ).resolveColumns(metricElementNames)

    intercept[InvalidMetricTimeSeriesException] {
      throw actual.left.get
    }
  }
  "指定したメトリクス名のみを解決する" in {
    import RootSpec._

    val names = MetricElementNames(
      Map(
        "media" ->
        Seq(
          "media_views"
        )
      )
    )

    val actual = createRoot(
      Request(
        params = Params(
          start_time = StatsDateTime("2017-01-01T01:01:01Z"),
          end_time = StatsDateTime("2017-01-01T01:01:01Z"),
          placement = ""
        )
      )
    ).resolveColumns(names)

    val expected = Seq(
      Column(
        "123",
        LocalDate.of(2017, 1, 1),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views" -> Some(1)
          )
        )
      ),
      Column(
        "123",
        LocalDate.of(2017, 1, 1),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views" -> Some(10)
          )
        )
      ),
      Column(
        "456",
        LocalDate.of(2017, 1, 1),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views" -> Some(1)
          )
        )
      ),
      Column(
        "456",
        LocalDate.of(2017, 1, 1),
        None,
        "",
        Map(
          "media" -> Map(
            "media_views" -> Some(10)
          )
        )
      )
    )
    val rightValue = actual.right.value

    assert(rightValue == expected)
  }
}

object RootSpec {

  val defaultRoot: Root = createRoot(
    Request(
      params = Params(
        start_time = StatsDateTime("2017-01-01T01:01:01Z"),
        end_time = StatsDateTime("2017-01-03T01:01:01Z"),
        placement = ""
      )
    )
  )

  def createRoot(request: Request): Root = {
    Root(
      data = Seq(
        Data(
          id = "123",
          id_data = Seq(
            IDData(
              metrics = Metrics(
                Map(
                  "billed_engagements"            -> Some(Vector(1, 2)),
                  "billed_charge_local_micro"     -> Some(Vector(1, 2)),
                  "media_views"                   -> Some(Vector(1, 2)),
                  "media_engagements"             -> None,
                  "conversion_purchases_assisted" -> Some(Vector(1, 2))
                )
              ),
              segment = None
            ),
            IDData(
              metrics = Metrics(
                Map(
                  "billed_engagements"            -> Some(Vector(10, 20)),
                  "billed_charge_local_micro"     -> Some(Vector(10, 20)),
                  "media_views"                   -> Some(Vector(10, 20)),
                  "media_engagements"             -> None,
                  "conversion_purchases_assisted" -> Some(Vector(10, 20))
                )
              ),
              segment = None
            )
          )
        ),
        Data(
          id = "456",
          id_data = Seq(
            IDData(
              metrics = Metrics(
                Map(
                  "billed_engagements"            -> Some(Vector(1, 2)),
                  "billed_charge_local_micro"     -> Some(Vector(1, 2)),
                  "media_views"                   -> Some(Vector(1, 2)),
                  "media_engagements"             -> None,
                  "conversion_purchases_assisted" -> Some(Vector(1, 2))
                )
              ),
              segment = None
            ),
            IDData(
              metrics = Metrics(
                Map(
                  "billed_engagements"            -> Some(Vector(10, 20)),
                  "billed_charge_local_micro"     -> Some(Vector(10, 20)),
                  "media_views"                   -> Some(Vector(10, 20)),
                  "media_engagements"             -> None,
                  "conversion_purchases_assisted" -> Some(Vector(10, 20))
                )
              ),
              segment = None
            )
          )
        )
      ),
      request = request
    )
  }

  val metricElementNames = MetricElementNames(
    Map(
      "billing" -> Seq(
        "billed_engagements",
        "billed_charge_local_micro"
      ),
      "media" -> Seq(
        "media_views",
        "media_engagements"
      ),
      "web_conversion" -> Seq(
        "conversion_purchases.assisted"
      )
    )
  )
}
