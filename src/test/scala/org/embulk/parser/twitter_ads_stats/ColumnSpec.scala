package org.embulk.parser.twitter_ads_stats

import org.embulk.spi.`type`.Types
import org.embulk.spi.{Column => EmbulkColumn}

class ColumnSpec extends UnitSpec {
  "メトリクスグループのJSONがアルファベット順で JSON 型で生成される" in {
    val metricElementNames = MetricElementNames(
      Map(
        "video" -> Seq(
          "video_total_views",
          "video_views_25",
        ),
        "billing" -> Seq(
          "billed_engagements",
          "billed_charge_local_micro"
        )
      )
    )
    val actual = Column.createEmbulkColumns(metricElementNames)

    val expected = Seq(
      new EmbulkColumn(0, "id", Types.STRING),
      new EmbulkColumn(1, "date", Types.STRING),
      new EmbulkColumn(2, "segment", Types.JSON),
      new EmbulkColumn(3, "placement", Types.STRING),
      new EmbulkColumn(4, "billing", Types.JSON),
      new EmbulkColumn(5, "video", Types.JSON)
    )

    assert(actual == expected)
  }
}
