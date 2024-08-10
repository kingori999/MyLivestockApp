package com.example.mylivestock;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class AddBreedDialog extends DialogFragment {

    private EditText editTextBreedName;
    private String selectedAnimalType;
    private LivestockViewModel livestockViewModel;

    public AddBreedDialog(String selectedAnimalType) {
        this.selectedAnimalType = selectedAnimalType;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_breed);

        editTextBreedName = dialog.findViewById(R.id.edit_text_breed_name);
        Button buttonSave = dialog.findViewById(R.id.button_save);

        livestockViewModel = new ViewModelProvider(requireActivity()).get(LivestockViewModel.class);

        buttonSave.setOnClickListener(v -> saveBreed());

        return dialog;
    }

    private void saveBreed() {
        String breedName = editTextBreedName.getText().toString().trim();

        if (TextUtils.isEmpty(breedName)) {
            // Show error message
            return;
        }

        Breed breed = new Breed(selectedAnimalType, breedName);
        livestockViewModel.insertBreed(breed);

        dismiss();
    }
}
