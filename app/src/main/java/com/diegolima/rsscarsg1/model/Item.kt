package com.diegolima.rsscarsg1.model

data class Item(

    val title: String,
    val pubDate: String,
    val link: String,
    val guid: String,
    val author: String,
    val thumbnail: String,
    val description: String,
    val content: String,
    val enclosure: Object,
    val categories: List<String>

)



/*
public string  { get; set; }
public string  { get; set; }
public string  { get; set; }
public string  { get; set; }
public string  { get; set; }
public string  { get; set; }
public string  { get; set; }
public string  { get; set; }
public Enclosure  { get; set; }
public List<string>  { get; set; }*/
