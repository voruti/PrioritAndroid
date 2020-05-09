package voruti.prioritandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import voruti.priorit.Item;

public class ListFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity mainActivity = ((MainActivity) getActivity());
        mainActivity.setListFragment(this);

        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        List<Item> itemList = new ArrayList<>();
        RecyclerView.Adapter adapter = new ItemAdapter(itemList, item -> {
            mainActivity.setCurrentItem(item);

            NavHostFragment.findNavController(ListFragment.this)
                    .navigate(R.id.action_ListFragment_to_DetailFragment);
        });
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        try {
            while (mainActivity.getManager() == null)
                Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        itemList.addAll(mainActivity.getManager().getAllItems());

        mainActivity.switchTo(true);
    }
}
