package org.embulk.parser.twitter_ads_stats

import org.embulk.config.{Config, ConfigDefault, Task}

trait PluginTask extends Task {
  @Config("stop_on_invalid_record")
  @ConfigDefault("false")
  def getStopOnInvalidRecord: Boolean
}
