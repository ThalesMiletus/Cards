package com.example.cards.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cards.R
import com.example.cards.config.AppConfig
import com.example.cards.model.CardModel
import com.example.cards.viewmodel.CardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val galleryResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            cardViewModel.setAdjustingCardItemUrl(result.data?.data)
        }
    }

    private val cardViewModel: CardViewModel by viewModels()

    @Inject
    lateinit var cardsRvAdapter: CardListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCardsRv()
        initObservables()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            cardViewModel.setAdjustingCardItem(CardModel("", ""))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initCardsRv() {
        cardsRvAdapter.setCardViewModel(cardViewModel)
        findViewById<RecyclerView>(R.id.rv_cards).apply {
            adapter = cardsRvAdapter
            layoutManager = GridLayoutManager(this@MainActivity, AppConfig.CARD_RV_SPAN_COUNT)
        }
    }

    private fun initObservables() {
        cardViewModel.allCards.observe(this) { cards ->
            cards.let { cardsRvAdapter.submitList(it) }
        }

        cardViewModel.editingCard.observe(this) { card ->
            card?.let { showCardConfigurationDialog(card) }
        }
    }

    private fun showCardConfigurationDialog(card: CardModel) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.dialog_card_configuration)

        val titleTextView = bottomSheetDialog.findViewById<AppCompatEditText>(R.id.edt_card_title)
        titleTextView?.setText(card.title)

        val imageUrlTextView =
            bottomSheetDialog.findViewById<AppCompatEditText>(R.id.edt_card_image_url)
        imageUrlTextView?.setText(card.imageUrl)

        val imagePickerImageView =
            bottomSheetDialog.findViewById<ImageView>(R.id.iv_card_image_picker)
        imagePickerImageView?.let { imageView ->
            Glide.with(this)
                .load(card.imageUrl)
                .error(ContextCompat.getDrawable(this, R.drawable.tree))
                .into(imageView)
        }

        imagePickerImageView?.setOnClickListener {
            bottomSheetDialog.dismiss()
            openImagePicker()
        }

        bottomSheetDialog.findViewById<TextView>(R.id.tv_ok)?.setOnClickListener {
            val configuredCard =
                CardModel(titleTextView?.text.toString(), imageUrlTextView?.text.toString())
            configuredCard.id = card.id
            cardViewModel.insertCardToCardRepository(configuredCard)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.findViewById<TextView>(R.id.tv_cancel)?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setOnCancelListener {
            cardViewModel.setAdjustingCardItem(null)
        }

        bottomSheetDialog.show()
    }

    private fun openImagePicker() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        galleryResultLauncher.launch(photoPickerIntent)
    }
}