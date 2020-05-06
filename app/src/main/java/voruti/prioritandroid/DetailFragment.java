package voruti.prioritandroid;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import voruti.priorit.Item;

public class DetailFragment extends Fragment {

    private EditText editDate;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Item item = ((MainActivity) getActivity()).getCurrentItem();

        EditText textTitle = view.findViewById(R.id.inp_title);
        textTitle.setText(item.getTitle());

        EditText textText = view.findViewById(R.id.inp_text);
        textText.setText(item.getText());

        Spinner spinnerCategories = view.findViewById(R.id.inp_categories);
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ((MainActivity) getActivity()).getManager().getAllCategories().toArray(new String[]{}));
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapterCategories);

        Spinner spinnerPriority = view.findViewById(R.id.inp_priority);
        ArrayAdapter<String> adapterPriority = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new String[]{"VERY_LOW", "LOW", "MED", "HIGH", "VERY_HIGH"});
        adapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapterPriority);
        spinnerPriority.setSelection(5 - item.getPriority().getValue());

        editDate = view.findViewById(R.id.inp_date);
        editDate.setKeyListener(null);
        editDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                editDateTapAction();
        });
        editDate.setOnClickListener(v -> editDateTapAction());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(item.getEtaDate());
        editDate.setText(getString(R.string.date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));

        Switch switchDone = view.findViewById(R.id.inp_done);
        switchDone.setChecked(item.isDone());

               /* NavHostFragment.findNavController(DetailFragment.this)
                        .navigate(R.id.action_DetailFragment_to_ListFragment);*/
    }

    private void editDateTapAction() {
        // TODO?: because of this i18n does not work for string "date" !!!
        String fieldContent = editDate.getText().toString();
        String[] stringParts = fieldContent.split("-"); // no streams?! Whhaaat?
        int[] dateParts = new int[stringParts.length];
        for (int i = 0; i < stringParts.length; i++) {
            dateParts[i] = Integer.parseInt(stringParts[i]);
        }

        new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
            editDate.setText(getString(R.string.date, year, month + 1, dayOfMonth));
        }, dateParts[0], dateParts[1] - 1, dateParts[2]).show();
    }
}
