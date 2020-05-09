package voruti.prioritandroid;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import voruti.priorit.Item;
import voruti.priorit.Priority;

public class DetailFragment extends Fragment {

    private MainActivity mainActivity;

    private EditText textTitle;
    private EditText textText;
    private Spinner spinnerCategories;
    private Spinner spinnerPriority;
    private EditText editDate;
    private Switch switchDone;

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

        mainActivity = ((MainActivity) getActivity());
        mainActivity.setDetailFragment(this);

        Item item = mainActivity.getCurrentItem();

        textTitle = view.findViewById(R.id.inp_title);
        textTitle.setText(item.getTitle());

        textText = view.findViewById(R.id.inp_text);
        textText.setText(item.getText());

        spinnerCategories = view.findViewById(R.id.inp_categories);
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mainActivity.getManager().getAllCategories().toArray(new String[]{}));
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapterCategories);

        spinnerPriority = view.findViewById(R.id.inp_priority);
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
        editDate.setText(getString(R.string.date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));

        switchDone = view.findViewById(R.id.inp_done);
        switchDone.setChecked(item.isDone());

        mainActivity.switchTo(false);
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

    Item saveCurrent() {
        Item item = new Item();

        item.setuName(mainActivity.getCurrentItem().getuName());
        item.setTitle(textTitle.getText().toString());
        item.setText(textText.getText().toString());
//        item.setCategories();
        item.setPriority(Priority.valueOf(spinnerPriority.getSelectedItem().toString()));
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(editDate.getText().toString());
            item.setEtaDate(date);
        } catch (ParseException e) {
            Log.println(Log.ERROR, "voruti", "SimpleDateFormat error!");
            e.printStackTrace();
        }
        item.setDone(switchDone.isChecked());

        NavHostFragment.findNavController(DetailFragment.this)
                .navigate(R.id.action_DetailFragment_to_ListFragment);

        return item;
    }
}
