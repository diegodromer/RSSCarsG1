package com.diegolima.rsscarsg1.model

data class RSSObject(

    val status: String,
    val feed: Feed,
    val items:  List<Item>
)