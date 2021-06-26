package com.example.cards.viewmodel

import androidx.lifecycle.*
import com.example.cards.model.CardModel
import com.example.cards.repository.CardRepository
import kotlinx.coroutines.launch

class CardViewModel(private val repository: CardRepository) : ViewModel() {

    val allCards: LiveData<List<CardModel>> = repository.allCards.asLiveData()
    var selectedCard: MutableLiveData<CardModel> = MutableLiveData()


    fun insert(cardModel: CardModel) = viewModelScope.launch {
        repository.insert(cardModel)
    }

    fun onCardItemClicked(card: CardModel) {
        selectedCard.value = card
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