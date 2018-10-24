package eu.vincinity2020.p2p_parking.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindDrawable
import butterknife.BindView
import butterknife.ButterKnife
import eu.vincinity2020.p2p_parking.R

class SearchAdapter(private val context: Context,
                    private val onSearchItemClickedListenerListener: OnSearchItemClickedListener)
    : ListAdapter<SearchResult, SearchAdapter.ViewHolder>(ItemCallback()) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_search_result, viewGroup, false)
        return ViewHolder(view, onSearchItemClickedListenerListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, positon: Int) {
        viewHolder.update(getItem(positon))
    }


    class ViewHolder(itemView: View,
                     private val onSearchItemClickedListenerListener: OnSearchItemClickedListener)
        : RecyclerView.ViewHolder(itemView) {
        init {
            ButterKnife.bind(this, itemView)
        }

        @BindView(R.id.text_view_search_result)
        lateinit var textViewSearchResult: AppCompatTextView

        @BindView(R.id.image_view_search_result_favourite)
        lateinit var imageViewFavorite: AppCompatImageView

        @BindDrawable(R.drawable.ic_star_black_24dp)
        lateinit var star: Drawable

        @BindDrawable(R.drawable.ic_star_border_black_24dp)
        lateinit var starBorder: Drawable

        fun update(searchResult: SearchResult) {
            textViewSearchResult.text = searchResult.text

            if (searchResult.isFavorite) {
                imageViewFavorite.background = star
            } else {
                imageViewFavorite.background = starBorder
            }

            itemView.setOnClickListener {
                onSearchItemClickedListenerListener.onClick(searchResult)
            }
        }


    }

    class ItemCallback : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(result1: SearchResult, result2: SearchResult): Boolean {
            return (result1.text == result2.text) && (result1.isFavorite == result2.isFavorite)
        }

        override fun areContentsTheSame(result1: SearchResult, result2: SearchResult): Boolean {
            return result1.text == result2.text
        }

    }

    interface OnSearchItemClickedListener {
        fun onClick(searchResult: SearchResult)
    }
}