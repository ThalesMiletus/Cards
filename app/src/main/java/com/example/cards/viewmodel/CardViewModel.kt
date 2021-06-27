package com.example.cards.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.example.cards.model.CardModel
import com.example.cards.repository.CardRepository
import kotlinx.coroutines.launch

class CardViewModel(private val cardRepository: CardRepository) : ViewModel() {

    val allCards: LiveData<List<CardModel>> = cardRepository.allCards.asLiveData()
    private val _editingCard = MutableLiveData<CardModel?>()
    val editingCard: LiveData<CardModel?>
        get() = _editingCard

    fun insertCardToCardRepository(cardModel: CardModel) = viewModelScope.launch {
        cardRepository.insert(cardModel)
    }

    fun setAdjustingCardItem(card: CardModel?) {
        _editingCard.value = card
    }

    fun setAdjustingCardItemUrl(imageUri: Uri?) {
        imageUri?.let {
            val card: CardModel? = _editingCard.value?.copy()
            card?.apply {
                imageUrl = imageUri.toString()
                id = _editingCard.value?.id
                _editingCard.value = this
            }
        }
    }
}

class CardViewModelFactory(private val repository: CardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}