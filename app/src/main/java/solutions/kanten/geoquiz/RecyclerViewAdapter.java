package solutions.kanten.geoquiz;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import solutions.kanten.geoquiz.models.Capital;
import solutions.kanten.geoquiz.models.Country;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private View view;
    private String capitalName;
    private ArrayList<Capital> capitalList = new ArrayList<>();
    private ArrayList<Country> countriesPassed = new ArrayList<>();
    private ArrayList<Country> countriesFailed= new ArrayList<>();
    private Country correctCountry;
    private Context mContext;
    private QuizActivity quizActivity;

    public RecyclerViewAdapter(ArrayList<Capital> capitalList, Context mContext, Country correctCountry) {
        this.capitalList = capitalList;
        this.mContext = mContext;
        this.correctCountry = correctCountry;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_capitals, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        if (i % 2 == 0) {
            view.setBackgroundResource(R.color.offColor);
        }
        String capital = capitalList.get(i).getCapitalName();
        viewHolder.txtCapital.setText(capital);
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Capital clickedCapital = capitalList.get(viewHolder.getAdapterPosition());
                if (clickedCapital.getCountry() == correctCountry) {
                    countriesPassed.add(correctCountry);
                    if (mContext instanceof QuizActivity) {
                        ((QuizActivity)mContext).refreshList();
                    }
                } else {

                    Toast.makeText(mContext, String.format(Locale.getDefault(),
                            "Incorrect! %s is the capital of %s", clickedCapital.getCapitalName(),
                            clickedCapital.getCountry().getCountryName()),
                            Toast.LENGTH_SHORT).show();
                    countriesFailed.add(correctCountry);
                }
            }
        });
        
    }

    @Override
    public int getItemCount() {
        return capitalList.size();
    }

    public View getView() {
        return view;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCapital;
        ConstraintLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCapital = itemView.findViewById(R.id.txtCapitalName);
            parentLayout = itemView.findViewById(R.id.parentLayoutListItem);
        }
    }

}
