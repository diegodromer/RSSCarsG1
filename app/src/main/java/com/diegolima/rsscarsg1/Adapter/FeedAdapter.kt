package com.diegolima.rsscarsg1.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.diegolima.rsscarsg1.Interface.ItemClickListener
import com.diegolima.rsscarsg1.R
import com.diegolima.rsscarsg1.model.RSSObject
import com.diegolima.rsscarsg1.network.loadImage

class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
                                       View.OnClickListener,
                                       View.OnLongClickListener {

    var txtTitle: TextView
    var txtPubdate: TextView
    var txtContent: TextView
    val ivNews: ImageView

    private var itemClickListener: ItemClickListener? = null

    init {
        txtTitle = itemView.findViewById(R.id.txtTitle) as TextView
        txtPubdate = itemView.findViewById(R.id.txtPubdate) as TextView
        txtContent = itemView.findViewById(R.id.txtContentDesc) as TextView
        ivNews = itemView.findViewById(R.id.ivNews) as ImageView

        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun onClick(v: View?) {
        itemClickListener!!.onClick(v, adapterPosition, false)
    }

    override fun onLongClick(v: View?): Boolean {
        itemClickListener!!.onClick(v, adapterPosition, true)
        return true
    }

}

class FeedAdapter (private val rssObject: RSSObject, private val mContext: Context): RecyclerView.Adapter<FeedViewHolder>(){

    private val inflate: LayoutInflater

    init {
        inflate = LayoutInflater.from(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = inflate.inflate(R.layout.row, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rssObject.items.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.txtTitle.text = rssObject.items[position].title
        holder.txtPubdate.text = rssObject.items[position].pubDate
        holder.txtContent.text = contentTrimDescrition(position)

        val urlImg = rssObject.items[position].thumbnail
        try {
            loadImage(mContext, urlImg, holder.ivNews)
        }catch (e : Exception){
            Toast.makeText(mContext, "Erro ao carregar: $e", Toast.LENGTH_SHORT).show()
        }

        holder.setItemClickListener(ItemClickListener { view, position, isLongClick ->
            if(!isLongClick){
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(rssObject.items[position].link))
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                mContext.startActivity(browserIntent)
            }else{
                Toast.makeText(mContext, rssObject.items[position].title, Toast.LENGTH_SHORT).show()
            }
        })
    }

    //removendo dados que nao sejam a descricao da noticia
    private fun contentTrimDescrition(position: Int): String {
        var strDesc = rssObject.items[position].content
        var action = false
        var newDesc = "" //descricao
        var verification = "<br>"
        for (item in strDesc.toCharArray()) {
            if (item.equals('>')) {
                if (verification.contains(">>"))
                    action = true
                else verification += ">"
            }

            if (action && (!item.equals('>'))) {
                newDesc += item
                if (newDesc.equals("   ")) {
                    newDesc = ""
                }
            }
        }
        action = false
        return newDesc
    }
}