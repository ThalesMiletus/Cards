package com.example.cards.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cards.CardsApplication
import com.example.cards.R
import com.example.cards.config.AppConfig
import com.example.cards.model.CardModel
import com.example.cards.viewmodel.CardViewModel
import com.example.cards.viewmodel.CardViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    private val galleryResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            onImageSelectedFromGallery(result.data)
        }
    }

    private val cardViewModel: CardViewModel by viewModels {
        CardViewModelFactory((application as CardsApplication).repository)
    }

    private val adapter by lazy {
        CardListAdapter(cardViewModel)
    }

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
            showCardConfigurationDialog(null)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initCardsRv() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_cards)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, AppConfig.CARD_RV_SPAN_COUNT)
    }

    private fun initObservables() {
        cardViewModel.allCards.observe(this) { cards ->
            cards.let { adapter.submitList(it) }
        }

        cardViewModel.selectedCard.observe(this) { card ->
            card?.let {
                showCardConfigurationDialog(card)
            }
        }
    }

    private fun showCardConfigurationDialog(card: CardModel?) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.dialog_card_configuration)
        val title = bottomSheetDialog.findViewById<TextView>(R.id.edt_card_title)
        val imageUrl = bottomSheetDialog.findViewById<TextView>(R.id.edt_card_image_url)
        val imagePicker = bottomSheetDialog.findViewById<ImageView>(R.id.iv_card_image_picker)
        card?.let {
            title?.text = card.title
            imageUrl?.text = card.imageUrl
            imagePicker?.let { it1 ->
                Glide.with(this)
                    .load(card.imageUrl)
                    .error(ContextCompat.getDrawable(this, R.drawable.tree))
                    .into(it1)
            }
        }

        bottomSheetDialog.findViewById<ImageView>(R.id.iv_card_image_picker)?.setOnClickListener {
            bottomSheetDialog.dismiss()
            openImagePicker()
        }

        bottomSheetDialog.findViewById<TextView>(R.id.tv_ok)?.setOnClickListener {
            val configuredCard = CardModel(title?.text.toString(), imageUrl?.text.toString())
            card?.let {
                configuredCard.id = card.id
            }
            cardViewModel.insert(configuredCard)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.findViewById<TextView>(R.id.tv_cancel)?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setOnCancelListener {
            cardViewModel.selectedCard.value = null
        }
        bottomSheetDialog.show()
    }

    private fun openImagePicker() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        galleryResultLauncher.launch(photoPickerIntent)
    }

    private fun onImageSelectedFromGallery(intent: Intent?) {
        intent?.let {
            val imageUri: Uri? = intent.data
            val card: CardModel? = cardViewModel.selectedCard.value
            card?.let {
                card.imageUrl = imageUri.toString()
            }
            showCardConfigurationDialog(card)
        }
    }
}