package com.koolyun.mis.adapter;

 
import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koolyun.mis.R;
import com.koolyun.mis.core.ShoppingCart;
import com.koolyun.mis.core.TotalMoneyObserver;
import com.koolyun.mis.core.order.OrderContentData;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.widget.MoneyView;

public class OrderDetailAdapter extends ImageCacheBaseAdapter implements TotalMoneyObserver{
 
	    private List<OrderContentData> mOrderContentData = null;
	    
	    public OrderDetailAdapter(Context context,List<OrderContentData> mOrderContentData){
	    	super(context);
	    	this.mOrderContentData = mOrderContentData;
	    }
		
	    public OrderDetailAdapter(Context context){
	    	super(context);

	    }
	    
	    public void setListData(List<OrderContentData> mOrderContentData)
	    {
	    	this.mOrderContentData = mOrderContentData;
	    }
	    
		@Override
		public int getCount() {
			if(mOrderContentData != null)
				return (mOrderContentData.size()+2 -1)/2;
			else
				return 0;
		}
		
		@Override
		public Object getItem(int position) {
			return mOrderContentData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if(convertView == null)
			{
				holder = new ViewHolder();
				convertView = listInflater.inflate(R.layout.leftbar_product_item_inf, null);
				holder.showImagepicture = (ImageView) convertView.findViewById(R.id.sale_product_item_imageview);
				holder.showTextViewname = (TextView) convertView.findViewById(R.id.sale_product_item_name);
				holder.showTextViwnumber = (TextView) convertView.findViewById(R.id.sale_product_item_nuber);
				holder.showTextViewprice = (MoneyView) convertView.findViewById(R.id.sale_product_item_price);
				holder.showTextViewname2 = (TextView) convertView.findViewById(R.id.sale_product_item_name2);
				holder.showTextViewprice2 = (MoneyView) convertView.findViewById(R.id.sale_product_item_price2);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(position < getCount()-1){
				//倒数第二行，不用判断，每行都是2个
				if(mOrderContentData.get(position*2).getProductId() == ShoppingCart.MANUID && 
						mOrderContentData.get(position*2).getmOrderContentRemark() != null &&
						mOrderContentData.get(position*2).getmOrderContentRemark().getRemark() != null &&
						!mOrderContentData.get(position*2).getmOrderContentRemark().getRemark().isEmpty())
				{
					holder.showTextViewname.setText((CharSequence)mOrderContentData.get(position*2).getmOrderContentRemark().getRemark());
				}
				else
				{
					holder.showTextViewname.setText((CharSequence) mOrderContentData.get(position*2).getOrderContentName().replace('\\', ' '));
				}
				
				if(mOrderContentData.get(position*2+1).getProductId() == ShoppingCart.MANUID && 
						mOrderContentData.get(position*2+1).getmOrderContentRemark() != null &&
						mOrderContentData.get(position*2+1).getmOrderContentRemark().getRemark() != null &&
						!mOrderContentData.get(position*2+1).getmOrderContentRemark().getRemark().isEmpty())
				{
					holder.showTextViewname2.setText((CharSequence)mOrderContentData.get(position*2+1).getmOrderContentRemark().getRemark());
				}
				else
				{
					holder.showTextViewname2.setText((CharSequence) mOrderContentData.get(position*2+1).getOrderContentName().replace('\\', ' '));
				}
				
				//holder.showTextViwnumber.setText(""+mOrderContentData.get(position).getCount());
				holder.showTextViewprice.setMoney(mOrderContentData.get(position*2).getOrderContent().getAmount());
				holder.showTextViewprice2.setMoney(mOrderContentData.get(position*2+1).getOrderContent().getAmount());
				//setImageView(holder,position);
			}else if(position ==(getCount() -1)){
				if(position*2 < mOrderContentData.size()){
					if(mOrderContentData.get(position*2).getProductId() == ShoppingCart.MANUID && 
							mOrderContentData.get(position*2).getmOrderContentRemark() != null &&
							mOrderContentData.get(position*2).getmOrderContentRemark().getRemark() != null &&
							!mOrderContentData.get(position*2).getmOrderContentRemark().getRemark().isEmpty())
					{
						holder.showTextViewname.setText((CharSequence)mOrderContentData.get(position*2).getmOrderContentRemark().getRemark());
					}
					else
					{
						holder.showTextViewname.setText((CharSequence) mOrderContentData.get(position*2).getOrderContentName().replace('\\', ' '));
					}
					holder.showTextViewprice.setMoney(mOrderContentData.get(position*2).getOrderContent().getAmount());
				}
				if((position*2+1) < mOrderContentData.size()){
					if(mOrderContentData.get(position*2+1).getProductId() == ShoppingCart.MANUID && 
							mOrderContentData.get(position*2+1).getmOrderContentRemark() != null &&
							mOrderContentData.get(position*2+1).getmOrderContentRemark().getRemark() != null &&
							!mOrderContentData.get(position*2+1).getmOrderContentRemark().getRemark().isEmpty())
					{
						holder.showTextViewname2.setText((CharSequence)mOrderContentData.get(position*2+1).getmOrderContentRemark().getRemark());
					}
					else
					{
						holder.showTextViewname2.setText((CharSequence) mOrderContentData.get(position*2+1).getOrderContentName().replace('\\', ' '));
					}
					holder.showTextViewprice2.setMoney(mOrderContentData.get(position*2+1).getOrderContent().getAmount());
				}
			}
			return convertView; 
		}

		protected void setImageView(ViewHolder viewHolder, int position) {
			
			String imageName = mOrderContentData.get(position).getPhoto();
			int productid = mOrderContentData.get(position).getProductId();
			File f = new File(Common.getRealImagePath(imageName));
			if(imageName == null || imageName.isEmpty() || !f.exists())
			{
				if(productid == 0)
				{
					imageName = "MANU_INPUT_DEFAULT";
				}
				else
				{
					imageName = "IMAGE_DEFAULT";
				}
				
			}
			

			Bitmap bitmap = getBitmapFromMemCache(imageName);
			if (bitmap == null) {
				if(imageName.equals("MANU_INPUT_DEFAULT"))
				{
					bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cart_manual);
				}
				else if(imageName.equals("IMAGE_DEFAULT"))
				{
					bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_product);
				}
				else
					bitmap = Common.zoomBitmap(BitmapFactory.decodeFile(Common.getRealImagePath(imageName),mBitmapOption),100,100);
				addBitmapToMemoryCache(imageName, bitmap);
			}
			viewHolder.showImagepicture.setImageBitmap(bitmap);
		}
		
		@Override
		public void totalMoneyChanged(String money) {
			this.notifyDataSetChanged();
			
		}
		
		public class ViewHolder {
			 public ImageView showImagepicture;
			 public TextView showTextViewname;
			 public TextView showTextViwnumber;
			 public MoneyView showTextViewprice;
			 public TextView showTextViewname2;
			 public MoneyView showTextViewprice2;
		}
}
