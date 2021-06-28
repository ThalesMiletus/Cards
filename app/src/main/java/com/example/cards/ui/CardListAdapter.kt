package com.example.cards.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cards.databinding.RvItemCardBinding
import com.example.cards.model.CardModel
import com.example.cards.util.hideKeyboard
import com.example.cards.viewmodel.CardViewModel

class CardListAdapter : ListAdapter<CardModel, CardListAdapter.CardViewHolder>(CardsComparator()) {

    private var cardViewModel: CardViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position), cardViewModel)
    }

    fun setCardViewModel(viewModel: CardViewModel) {
        cardViewModel = viewModel
    }

    class CardViewHolder(view: RvItemCardBinding) : RecyclerView.ViewHolder(view.root) {

        private var binding: RvItemCardBinding = view

        fun bind(cardModel: CardModel, viewModel: CardViewModel?) {
            binding.apply {
                card = cardModel
                cardViewModel = viewModel
                edtCardTitle.apply {
                    setOnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            hideKeyboard()
                            clearFocus()
                            if (cardModel.title != text.toString()) {
                                cardModel.title = text.toString()
                                viewModel?.insertCardToCardRepository(cardModel)
                            }
                        }
                        (actionId == EditorInfo.IME_ACTION_DONE)
                    }
                }
                executePendingBindings()
            }
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
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CardModel, newItem: CardModel): Boolean {
            return oldItem == newItem
        }
    }
}