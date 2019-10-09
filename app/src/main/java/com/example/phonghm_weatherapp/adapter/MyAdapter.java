package com.example.phonghm_weatherapp.adapter;

import android.view.LayoutInflater;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.phonghm_weatherapp.databinding.CityItemBinding;

import com.example.phonghm_weatherapp.viewmodel.ShowViewModel;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    LayoutInflater layoutInflater;

    private List<ShowViewModel> newsList;

    public MyAdapter(List<ShowViewModel> newsList)
    {
        this.newsList = newsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {


        if(layoutInflater == null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        final CityItemBinding cityItemBinding  = CityItemBinding.inflate(layoutInflater,parent,false);
        return new MyViewHolder(cityItemBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ShowViewModel showViewModel = newsList.get(position);
        holder.bind(showViewModel);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CityItemBinding cityItemBinding;
        // TextView title, desc;
        public MyViewHolder(CityItemBinding newsBinding) {
            super(newsBinding.getRoot());
            this.cityItemBinding = newsBinding;
            //title = (TextView)itemView.findViewById(R.id.titleval);
            //desc =(TextView)itemView.findViewById(R.id.descval);

        }

        public void bind(ShowViewModel showViewModel)
        {
            this.cityItemBinding.setViewmodel(showViewModel);
        }

        public CityItemBinding getCityItemBinding()
        {
            return cityItemBinding;
        }

    }
}

