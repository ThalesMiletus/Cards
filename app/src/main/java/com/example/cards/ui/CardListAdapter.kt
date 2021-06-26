package com.example.cards.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cards.databinding.RvItemCardBinding
import com.example.cards.model.CardModel
import com.example.cards.viewmodel.CardViewModel

class CardListAdapter(private val cardViewModel: CardViewModel) :
    ListAdapter<CardModel, CardListAdapter.CardViewHolder>(CardsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = getItem(position)
        holder.bind(card, cardViewModel)
    }

    class CardViewHolder(view: RvItemCardBinding) : RecyclerView.ViewHolder(view.root) {

        private var binding: RvItemCardBinding = view

        fun bind(card: CardModel, cardViewModel: CardViewModel) {
            binding.card = card
            binding.cardViewModel = cardViewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CardViewHolder {
                val binding: RvItemCardBinding = RvItemCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CardViewHolder(binding)
            }
        }
    }

    class CardsComparator : DiffUtil.ItemCallback<CardModel>() {
        override fun areItemsTheSame(oldItem: CardModel, newItem: CardModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CardModel, newItem: CardModel): Boolean {
            return oldItem == newItem
        }
    }
}