Embulk::JavaPlugin.register_parser(
  "twitter_ads_stats", "org.embulk.parser.twitter_ads_stats.TwitterAdsStatsParserPlugin",
  File.expand_path('../../../../classpath', __FILE__))
