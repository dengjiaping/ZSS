package com.koolyun.mis.core.product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.koolyun.mis.R;
import com.koolyun.mis.core.ManagerBase;
import com.koolyun.mis.core.ShoppingCart;
import com.koolyun.mis.core.order.Onsale;
import com.koolyun.mis.util.Common;

public class ProductManager extends ManagerBase {

	public static Product getCustomProduct(String price) {
		return new Product(ShoppingCart.MANUID, -1, Common.getString(R.string.manual_input), price, "", 1);
	}

	/**
	 * 保存一条商品信息到数据库，如果是修改商品信息，修改后的商品信息不会覆盖修改前的，而是会重新存储一条
	 * 同时更新ProductPinyin，ProductToAttribute表
	 * 
	 * @param mProductData
	 * @return -1：失败 0：成功
	 */
	public static int saveProductData(ProductData mProductData) {
		if (mProductData == null)
			return -1;
		if (productAlreadyIn(mProductData.getProduct()))
			return -1;

		ContentValues values = new ContentValues();
		values.put("name", mProductData.getProduct().getName());
		values.put("productPhoto", mProductData.getProduct().getProductPhoto());
		values.put("price", mProductData.getProduct().getPrice());
		values.put("productCategoryID", mProductData.getProduct().getProductCategoryID());
		values.put("enable", 1);
		values.put("syncFlag", 0);

		int id = -1;

		msqlitedb.beginTransaction();

		try {
			if (productHasSame(mProductData.getProduct())) {
				id = (int) msqlitedb.insertWithOnConflict("Product", null, values, SQLiteDatabase.CONFLICT_REPLACE);
				String sql0 = "UPDATE `Product` SET enable=0,syncFlag=1 where `productID` = "
						+ mProductData.getProduct().getProductID();
				msqlitedb.execSQL(sql0);
				String sql1 = "UPDATE `ProductToAttribute` SET enable=0 where `productID` = "
						+ mProductData.getProduct().getProductID();
				msqlitedb.execSQL(sql1);
			} else {
				id = (int) msqlitedb.insertWithOnConflict("Product", null, values, SQLiteDatabase.CONFLICT_REPLACE);
			}

			Product mc = mProductData.getProduct();
			ContentValues values2 = new ContentValues();
			values2.put("productId", String.valueOf(id));
			int cataid = mc.getProductCategoryID();
			values2.put("productCategoryID", String.valueOf(cataid));
			String proname = mc.getName();
			values2.put("productName", String.valueOf(proname));
			String firstChar = Common.getFirstPinYinHeadChar(proname);
			values2.put("firstChar", String.valueOf(firstChar));
			values2.put("enable", 1);

			if (mProductData.getProduct().getProductID() > 0) {
				msqlitedb.insertWithOnConflict("ProductPinyin", null, values2, SQLiteDatabase.CONFLICT_IGNORE);
				String sql0 = "UPDATE `ProductPinyin` SET enable=0  where `productID` = "
						+ mProductData.getProduct().getProductID();
				msqlitedb.execSQL(sql0);
			} else {
				msqlitedb.insertWithOnConflict("ProductPinyin", null, values2, SQLiteDatabase.CONFLICT_IGNORE);
			}

			mProductData.getProduct().setProductID(id);
			for (int i = 0; i < mProductData.getAttrList().size(); i++) {
				ContentValues valuesAttr = new ContentValues();
				valuesAttr.put("productID", id);
				valuesAttr.put("productAttributeID", mProductData.getAttrList().get(i).getProductAttributeID());
				valuesAttr.put("enable", 1);
				msqlitedb.insertWithOnConflict("ProductToAttribute", null, valuesAttr, SQLiteDatabase.CONFLICT_REPLACE);
			}
			msqlitedb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msqlitedb.endTransaction();
		}
		return 0;
	}

	/**
	 * 删除id为productCate的商品分类
	 * 
	 * @param productCate
	 */
	public static void deleteProductCate(int productCate) {
		// 注意：Product是ProductCategory的从表
		// 删除数据库表时，应先删除从表，再删主表
		msqlitedb.beginTransaction(); // 设置事物（Transaction），保证dml数据操作的完整性
		try {
			String sql = "UPDATE `Product` SET productCategoryID=0,syncFlag=1 where `productCategoryID` = "
					+ productCate;
			msqlitedb.execSQL(sql);
			String sql0 = "UPDATE `ProductCategory` SET enable=0,syncFlag=1 where `productCategoryID` = " + productCate;
			msqlitedb.execSQL(sql0);
			msqlitedb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msqlitedb.endTransaction();
		}
	}

	/**
	 * 增加一个商品分类
	 * 
	 * @param mProductCategory
	 * @return -1：增加失败 其他值：增加成功
	 */
	public static int addProductCate(ProductCategory mProductCategory) {
		if (productCateAlreadyIn(mProductCategory))
			return -1;
		ContentValues values = new ContentValues();
		values.put("name", mProductCategory.getProductCategoryName());
		values.put("enable", mProductCategory.getEnable());
		values.put("syncFlag", 0);
		return (int) msqlitedb.insertWithOnConflict("ProductCategory", null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	/**
	 * 更新一个商品分类
	 * 
	 * @param mProductCategory
	 * @return
	 */
	public static int updateProductCate(ProductCategory mProductCategory) {
		ContentValues values = new ContentValues();
		values.put("name", mProductCategory.getProductCategoryName());
		values.put("enable", 1);
		values.put("syncFlag", 0);
		msqlitedb.beginTransaction();

		try {
			int id = (int) msqlitedb.insertWithOnConflict("ProductCategory", null, values,
					SQLiteDatabase.CONFLICT_REPLACE);

			String sql0 = "UPDATE `Product` SET productCategoryID=" + id + " , syncFlag=1 where `productCategoryID` = "
					+ mProductCategory.getProductCategoryId();
			msqlitedb.execSQL(sql0);

			String sql1 = "UPDATE `ProductCategory` SET enable=0,syncFLag=1 where `productCategoryID` = "
					+ mProductCategory.getProductCategoryId();
			msqlitedb.execSQL(sql1);
			msqlitedb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msqlitedb.endTransaction();
		}
		return 0;
	}

	/**
	 * 检测此商品分类的=========名字===========是否已存在
	 * 
	 * @param mProductCategory
	 * @return
	 */
	public static boolean productCateAlreadyIn(ProductCategory mProductCategory) {
		String sql = "SELECT COUNT(*) FROM `ProductCategory` where productCategoryID != "
				+ mProductCategory.getProductCategoryId() + " and name = '" + mProductCategory.getProductCategoryName()
				+ "'" + " and enable=1";

		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return false;
		}
		int count = result.getInt(0);
		result.close();
		if (count > 0)
			return true;
		else
			return false;
	}

	/**
	 * 检测此商品分类的=========id==========是否已存在
	 * 
	 * @param mProductCategory
	 * @return
	 */
	public static boolean productCateHasSame(ProductCategory mProductCategory) {
		String sql = "SELECT COUNT(*) FROM `ProductCategory` where productCategoryID = "
				+ mProductCategory.getProductCategoryId() + " and enable=1";
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return false;
		}
		int count = result.getInt(0);
		result.close();
		if (count > 0)
			return true;
		else
			return false;
	}

	public static int saveProduct(Product mProduct) {
		if (productAlreadyIn(mProduct)) {
			return updateProduct(mProduct);
		} else {
			return addProduct(mProduct);
		}
	}

	public static int addProduct(Product mProduct) {
		ContentValues values = new ContentValues();

		values.put("name", mProduct.getName());
		values.put("productPhoto", mProduct.getProductPhoto());
		values.put("productCategoryID", mProduct.getProductCategoryID());
		values.put("enable", 1);
		values.put("syncFlag", 0);
		return (int) msqlitedb.insertWithOnConflict("Product", null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public static int updateProduct(Product mProduct) {
		ContentValues values = new ContentValues();
		values.put("name", mProduct.getName());
		values.put("productPhoto", mProduct.getProductPhoto());
		values.put("productCategoryID", mProduct.getProductCategoryID());
		values.put("enable", 1);
		values.put("syncFlag", 0);

		int id = 0;
		msqlitedb.beginTransaction();

		try {
			id = (int) msqlitedb.insertWithOnConflict("Product", null, values, SQLiteDatabase.CONFLICT_REPLACE);

			String sql0 = "UPDATE `ProductToAttribute` SET productID=" + id + " where `productID` = "
					+ mProduct.getProductID();

			String sql1 = "UPDATE `Product` SET enable = 0,syncFlag=1 where `productID` = " + mProduct.getProductID();
			msqlitedb.execSQL(sql0);
			msqlitedb.execSQL(sql1);
			msqlitedb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msqlitedb.endTransaction();
		}
		return id;
	}

	public static void deleteProduct(Product mProduct) {
		String sql0 = "UPDATE `Product` SET enable=0,syncFlag=1 where `productID` = " + mProduct.getProductID();

		String sql1 = "UPDATE `ProductToAttribute` SET enable=0 where `productID` = " + mProduct.getProductID();

		msqlitedb.beginTransaction();

		try {
			msqlitedb.execSQL(sql0);
			msqlitedb.execSQL(sql1);
			msqlitedb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msqlitedb.endTransaction();
		}
	}

	public static int saveProductAttr(ProductAttribute mProductAttr) {
		if (productAttrAlreadyIn(mProductAttr))
			return -1;
		if (productAttrHasSame(mProductAttr)) {
			return updateProductAttr(mProductAttr);
		} else {
			return addProductAttr(mProductAttr);
		}
	}

	public static int addProductAttr(ProductAttribute mProductAttr) {
		ContentValues values = new ContentValues();

		values.put("name", mProductAttr.getName());
		values.put("choiceType", mProductAttr.getChoiceType());
		values.put("defaultChoice", mProductAttr.getDefaultChoice());
		values.put("enable", 1);

		return (int) msqlitedb.insertWithOnConflict("ProductAttribute", null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public static int updateProductAttr(ProductAttribute mProductAttr) {
		int id = 0;
		msqlitedb.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			values.put("name", mProductAttr.getName());
			values.put("choiceType", mProductAttr.getChoiceType());
			values.put("defaultChoice", mProductAttr.getDefaultChoice());
			values.put("enable", 1);

			id = (int) msqlitedb
					.insertWithOnConflict("ProductAttribute", null, values, SQLiteDatabase.CONFLICT_REPLACE);

			String sql0 = "UPDATE `ProductToAttribute` SET productAttributeID=" + id + " where `productAttributeID` = "
					+ mProductAttr.getProductAttributeID();
			msqlitedb.execSQL(sql0);

			String sql1 = "UPDATE `ProductAttribute` SET enable=0,syncFlag=1 where `productAttributeID` = "
					+ mProductAttr.getProductAttributeID();
			msqlitedb.execSQL(sql1);
			msqlitedb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msqlitedb.endTransaction();
		}
		return id;
	}

	/**
	 * 删除商品属性操作，对应在数据库的行为是把此属性的enable设为0,syncFlag设为1
	 * 
	 * @param mProductAttr
	 */
	public static void deleteProductAttr(ProductAttribute mProductAttr) {
		msqlitedb.beginTransaction();
		try {
			String sql0 = "UPDATE `ProductAttribute` SET enable=0,syncFlag=1 where `productAttributeID` = "
					+ mProductAttr.getProductAttributeID();
			msqlitedb.execSQL(sql0);
			String sql1 = "UPDATE `ProductSubAttribute` SET enable=0,syncFlag=1 where `productAttributeID` = "
					+ mProductAttr.getProductAttributeID();
			msqlitedb.execSQL(sql1);
			msqlitedb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msqlitedb.endTransaction();
		}
	}

	public static void updateProductDefaultSub(ProductAttribute mProductAttr, int defaultid) {
		String sql0 = "UPDATE `ProductAttribute` SET defaultChoice=" + defaultid + " where `productAttributeID` = "
				+ mProductAttr.getProductAttributeID();
		msqlitedb.execSQL(sql0);
	}

	public static int saveProductSubAttr(ProductSubAttribute mProductSubAttr) {
		if (productSubAttrAlreadyIn(mProductSubAttr)) {
			return updateProductSubAttr(mProductSubAttr);
		} else {
			return addProductSubAttr(mProductSubAttr);
		}
	}

	public static int addProductSubAttr(ProductSubAttribute mProductSubAttr) {
		ContentValues values = new ContentValues();
		values.put("productAttributeID", mProductSubAttr.getProductAttributeID());
		values.put("name", mProductSubAttr.getName());
		values.put("priceAffect", mProductSubAttr.getPriceAffect());
		values.put("enable", 1);
		values.put("syncFlag", 0);

		return (int) msqlitedb.insertWithOnConflict("ProductSubAttribute", null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
	}

	public static int updateProductSubAttr(ProductSubAttribute mProductSubAttr) {
		ContentValues values = new ContentValues();
		values.put("productAttributeID", mProductSubAttr.getProductAttributeID());
		values.put("name", mProductSubAttr.getName());
		values.put("priceAffect", mProductSubAttr.getPriceAffect());
		values.put("enable", 1);
		values.put("syncFlag", 0);

		int id = 0;
		msqlitedb.beginTransaction();

		try {
			id = (int) msqlitedb.insertWithOnConflict("ProductSubAttribute", null, values,
					SQLiteDatabase.CONFLICT_REPLACE);

			String sql1 = "UPDATE `ProductSubAttribute` SET enable = 0,syncFlag=1 where `productSubAttributeId` = "
					+ mProductSubAttr.getProductSubAttributeId();
			msqlitedb.execSQL(sql1);
			msqlitedb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msqlitedb.endTransaction();
		}

		return id;
	}

	public static int saveOnSale(Onsale mOnsale) {
		if (OnSaleAlreadyIn(mOnsale)) {
			return -1;
		}
		if (OnSaleHasSame(mOnsale)) {
			return updateOnsale(mOnsale);
		} else {
			return addOnsale(mOnsale);
		}
	}

	public static int updateOnsale(Onsale mOnsale) {
		ContentValues values = new ContentValues();
		values.put("onsaleType", mOnsale.getOnsaleType());
		values.put("onsaleName", mOnsale.getOnsaleName());
		values.put("value", mOnsale.getValue());
		values.put("enable", 1);
		values.put("syncFlag", 0);

		int id = 0;
		msqlitedb.beginTransaction();

		try {
			id = (int) msqlitedb.insertWithOnConflict("Onsale", null, values, SQLiteDatabase.CONFLICT_REPLACE);

			String sql1 = "UPDATE `Onsale` SET enable = 0,syncFlag=1 where `onsaleID` = " + mOnsale.getOnsaleID();
			msqlitedb.execSQL(sql1);
			msqlitedb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msqlitedb.endTransaction();
		}
		return id;
	}

	public static int addOnsale(Onsale mOnsale) {
		ContentValues values = new ContentValues();
		values.put("onsaleType", mOnsale.getOnsaleType());
		values.put("onsaleName", mOnsale.getOnsaleName());
		values.put("value", mOnsale.getValue());
		values.put("enable", 1);
		values.put("syncFlag", 0);

		return (int) msqlitedb.insertWithOnConflict("Onsale", null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public static void deleteOnsale(Onsale mOnsale) {
		String sql0 = "UPDATE `Onsale` SET enable=0,syncFlag=1 where `onsaleID` = " + mOnsale.getOnsaleID();
		msqlitedb.execSQL(sql0);
	}

	public static boolean productAlreadyIn(Product mProduct) {
		String sql = "SELECT COUNT(*) FROM `Product` where `productID`!= " + mProduct.getProductID()
				+ " and `name` = '" + Common.getSqlString(mProduct.getName()) + "' and enable=1";
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return false;
		}
		int count = result.getInt(0);
		result.close();
		if (count > 0)
			return true;
		else
			return false;
	}

	public static boolean productHasSame(Product mProduct) {
		String sql = "SELECT COUNT(*) FROM `Product` where `productID`= " + mProduct.getProductID() + " and enable=1";
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return false;
		}
		int count = result.getInt(0);
		result.close();
		if (count > 0)
			return true;
		else
			return false;
	}

	public static boolean productAttrAlreadyIn(ProductAttribute mProductAttr) {
		String sql = "SELECT COUNT(*) FROM `ProductAttribute` where `productAttributeID`!= "
				+ mProductAttr.getProductAttributeID() + " and `name` = '"
				+ Common.getSqlString(mProductAttr.getName()) + "' and enable=1";

		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return false;
		}
		int count = result.getInt(0);
		result.close();
		if (count > 0)
			return true;
		else
			return false;
	}

	public static boolean productAttrHasSame(ProductAttribute mProductAttr) {
		String sql = "SELECT COUNT(*) FROM `ProductAttribute` where `productAttributeID`= "
				+ mProductAttr.getProductAttributeID() + " and  enable=1";

		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return false;
		}
		int count = result.getInt(0);
		result.close();
		if (count > 0)
			return true;
		else
			return false;
	}

	public static boolean productSubAttrAlreadyIn(ProductSubAttribute mProductSubAttr) {
		String sql = "SELECT COUNT(*) FROM `ProductSubAttribute` where `name` = '"
				+ Common.getSqlString(mProductSubAttr.getName()) + "'" + " and enable=1";

		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return false;
		}
		int count = result.getInt(0);
		result.close();
		if (count > 0)
			return true;
		else
			return false;
	}

	public static boolean OnSaleAlreadyIn(Onsale mOnsale) {
		String sql = "SELECT COUNT(*) FROM `Onsale` where `onsaleID`!= " + mOnsale.getOnsaleID()
				+ " and onsaleName = '" + Common.getSqlString(mOnsale.getOnsaleName()) + "'" + " and enable=1";

		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return false;
		}
		int count = result.getInt(0);
		result.close();
		if (count > 0)
			return true;
		else
			return false;
	}

	public static boolean OnSaleHasSame(Onsale mOnsale) {
		String sql = "SELECT COUNT(*) FROM `Onsale` where `onsaleID`= " + mOnsale.getOnsaleID() + "  and enable=1";

		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return false;
		}
		int count = result.getInt(0);
		result.close();
		if (count > 0)
			return true;
		else
			return false;
	}

	public static ProductCategory getProductCategory(int cateId) {
		String sql = "SELECT * FROM `ProductCategory` where `productCategoryId`= " + cateId + " and enable=1";
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return null;
		}
		int productCategoryId = result.getInt(0);
		String productCategoryName = result.getString(1);
		int enable = result.getInt(2);
		result.close();
		ProductCategory p = new ProductCategory(productCategoryId, productCategoryName, enable);
		return p;
	}

	public static ProductSubAttribute getDefaultSubAttr(int attrId) {
		String sql = "SELECT * FROM `ProductSubAttribute` where `enable`=1 and `productSubAttributeId` in "
				+ "(SELECT defaultChoice FROM `ProductAttribute` where `enable`=1 and productAttributeID = " + attrId
				+ ")";
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return null;
		}
		int productSubAttributeId = result.getInt(0);
		int productAttributeID = result.getInt(1);
		String name = result.getString(2);
		String priceAffect = result.getString(3);
		int enable = result.getInt(4);
		result.close();
		ProductSubAttribute p = new ProductSubAttribute(productSubAttributeId, productAttributeID, name, priceAffect,
				enable);
		return p;
	}

	public static List<ProductSubAttribute> getDefaultSubAttrList(int productId) {
		List<ProductAttribute> tmpList = getProductAttributeById(productId, true);
		if (tmpList == null)
			return null;
		List<ProductSubAttribute> tmpSubList = new LinkedList<ProductSubAttribute>();
		for (int i = 0; i < tmpList.size(); i++) {
			ProductSubAttribute tmp = getDefaultSubAttr(tmpList.get(i).getProductAttributeID());
			if (tmp != null)
				tmpSubList.add(tmp);
		}
		return tmpSubList;
	}

	public static ProductAttribute getProductAttributeByAttrId(int attrId) {
		String sql = "SELECT * FROM `ProductAttribute` where `enable`=1 and `productAttributeID` = " + attrId;
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return null;
		}
		ProductAttribute mProductAttribute = new ProductAttribute(result.getInt(0), result.getString(1),
				result.getInt(2), result.getInt(3), result.getInt(4));
		result.close();
		return mProductAttribute;
	}

	public static List<ProductAttribute> getProductAttributeById(int productId) {
		return getProductAttributeById(productId, false);
	}

	public static List<ProductAttribute> getProductAttributeById(int productId, boolean onlyenable) {
		String sql = "SELECT pa.*  FROM ProductAttribute pa,ProductToAttribute pta  where pta.productID="
				+ String.valueOf(productId) + (onlyenable ? " AND pa.enable=1 AND pta.enable=1 " : " ")
				+ " and pta.ProductAttributeID=pa.ProductAttributeID  order by pa.rowid ASC";

		Cursor result = msqlitedb.rawQuery(sql, null);
		List<ProductAttribute> productlist = new ArrayList<ProductAttribute>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			productlist.add(new ProductAttribute(result.getInt(0), result.getString(1), result.getInt(2), result
					.getInt(3), result.getInt(4)));
		}
		result.close();

		return productlist;
	}

	public static List<ProductAttribute> getProductAttributeForSync() {
		String sql = "SELECT * FROM `ProductAttribute` where syncFlag in (0,1) ORDER BY `productAttributeID` ASC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<ProductAttribute> productattributeslist = new LinkedList<ProductAttribute>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productAttributeID = result.getInt(0);
			String name = result.getString(1);
			int choiceType = result.getInt(2);
			int defaultChoice = result.getInt(3);
			int enable = result.getInt(4);
			int syncFlag = result.getInt(5);
			ProductAttribute p = new ProductAttribute(productAttributeID, name, choiceType, defaultChoice, enable);
			p.setSyncFlag(syncFlag);
			productattributeslist.add(p);
		}
		result.close();

		return productattributeslist;
	}

	public static List<ProductAttribute> getAllProductAttribute() {
		String sql = "SELECT * FROM `ProductAttribute` WHERE `enable`=1 ORDER BY name ASC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<ProductAttribute> productlist = new ArrayList<ProductAttribute>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			productlist.add(new ProductAttribute(result.getInt(0), result.getString(1), result.getInt(2), result
					.getInt(3), result.getInt(4)));
		}
		result.close();

		return productlist;
	}

	public static List<ProductAttribute> getAllProductAttributeForBackup(SQLiteDatabase sqlitedb) {
		String sql = "SELECT * FROM `ProductAttribute` where `enable`=1";
		Cursor result = sqlitedb.rawQuery(sql, null);
		List<ProductAttribute> productlist = new ArrayList<ProductAttribute>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			productlist.add(new ProductAttribute(result.getInt(0), result.getString(1), result.getInt(2), result
					.getInt(3), result.getInt(4)));
		}
		result.close();

		return productlist;
	}

	public static List<ProductSubAttribute> getProductSubAttributeByAttrId(int attrId) {
		String sql = "SELECT * FROM `ProductSubAttribute` where `enable`=1 and `productAttributeID` = " + attrId;
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<ProductSubAttribute> productlist = new LinkedList<ProductSubAttribute>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			productlist.add(new ProductSubAttribute(result.getInt(0), result.getInt(1), result.getString(2), result
					.getString(3), result.getInt(4)));
		}
		result.close();

		return productlist;
	}

	public static List<ProductSubAttribute> getProductsSubAttributesForSync() {
		String sql = "SELECT * FROM `ProductSubAttribute` where syncFlag in (0,1) ORDER BY `productSubAttributeID` ASC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<ProductSubAttribute> productsubattributelist = new LinkedList<ProductSubAttribute>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productSubAttributeId = result.getInt(0);
			int productAttributeID = result.getInt(1);
			String name = result.getString(2);
			String priceAffect = result.getString(3);
			int enable = result.getInt(4);
			int syncFlag = result.getInt(5);
			ProductSubAttribute p = new ProductSubAttribute(productSubAttributeId, productAttributeID, name,
					priceAffect, enable);
			p.setSyncFlag(syncFlag);
			productsubattributelist.add(p);
		}
		result.close();

		return productsubattributelist;
	}

	public static List<ProductSubAttribute> getAllProductsSubAttributesForBackup(SQLiteDatabase sqlitedb) {
		String sql = "SELECT * FROM `ProductSubAttribute` where `enable`=1";
		Cursor result = sqlitedb.rawQuery(sql, null);
		List<ProductSubAttribute> productsubattributelist = new LinkedList<ProductSubAttribute>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productSubAttributeId = result.getInt(0);
			int productAttributeID = result.getInt(1);
			String name = result.getString(2);
			String priceAffect = result.getString(3);
			int enable = result.getInt(4);
			int syncFlag = result.getInt(5);
			ProductSubAttribute p = new ProductSubAttribute(productSubAttributeId, productAttributeID, name,
					priceAffect, enable);
			p.setSyncFlag(syncFlag);
			productsubattributelist.add(p);
		}
		result.close();

		return productsubattributelist;
	}

	public static List<ProductCategory> getAllProductCategory() {

		String sql = "SELECT * FROM `ProductCategory` WHERE `enable`=1 ORDER BY name ASC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<ProductCategory> productlist = new LinkedList<ProductCategory>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productCategoryId = result.getInt(0);
			String productCategoryName = result.getString(1);
			int enable = result.getInt(2);
			ProductCategory p = new ProductCategory(productCategoryId, productCategoryName, enable);
			productlist.add(p);
		}
		result.close();

		return productlist;
	}

	public static List<ProductCategory> getAllProductCategoryForBackup(SQLiteDatabase sqlitedb) {
		String sql = "SELECT * FROM `ProductCategory` where `enable`=1";
		Cursor result = sqlitedb.rawQuery(sql, null);
		List<ProductCategory> productlist = new LinkedList<ProductCategory>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productCategoryId = result.getInt(0);
			String productCategoryName = result.getString(1);
			int enable = result.getInt(2);
			ProductCategory p = new ProductCategory(productCategoryId, productCategoryName, enable);
			productlist.add(p);
		}
		result.close();

		return productlist;
	}

	public static List<ProductCategory> getProductCategoryForSync() {
		String sql = "SELECT * FROM `ProductCategory` WHERE `syncFlag` in (0,1) ORDER BY `productCategoryID` ASC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<ProductCategory> productCategoryList = new LinkedList<ProductCategory>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productCategoryId = result.getInt(0);
			String productCategoryName = result.getString(1);
			int enable = result.getInt(2);
			int syncFlag = result.getInt(3);
			ProductCategory p = new ProductCategory(productCategoryId, productCategoryName, enable);
			p.setSyncFlag(syncFlag);
			productCategoryList.add(p);
		}
		result.close();

		return productCategoryList;
	}

	public static List<Product> getALLProductByCategory(int categoryId) {
		if (categoryId == 0)
			return getALLProduct();
		String sql = "SELECT * FROM `Product` where `enable`=1 and productCategoryID = " + String.valueOf(categoryId)
				+ " and productID > 0 ORDER BY name ASC ";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Product> productlist = new LinkedList<Product>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productID = result.getInt(0);
			String name = result.getString(1);
			String productPhoto = result.getString(2);
			String price = result.getString(3);
			int productCategoryID = result.getInt(4);
			int enable = result.getInt(5);
			int syncFlag = result.getInt(6);
			Product p = new Product(productID, productCategoryID, name, price, productPhoto, enable);
			p.setSyncFlag(syncFlag);
			productlist.add(p);
		}

		result.close();
		return productlist;
	}

	public static int getProductCountByCategory(int categoryId) {
		String tmpByCate = null;
		if (categoryId != 0)
			tmpByCate = "productCategoryID = " + String.valueOf(categoryId) + " and";
		else
			tmpByCate = "";
		String sql = "SELECT COUNT(*) FROM `Product` where `enable`=1 and " + tmpByCate
				+ " productID > 0 ORDER BY name ASC ";
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return 0;
		}
		int count = result.getInt(0);
		result.close();
		return count;
	}

	public static int getProductCountByAttr(int attrId) {
		String sql = "SELECT COUNT(*) FROM `ProductToAttribute` where  `enable`=1 and productAttributeID = " + attrId;
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst()) {
			result.close();
			return 0;
		}
		int count = result.getInt(0);
		result.close();
		return count;
	}

	public static List<Product> getALLProduct() {
		String sql = "SELECT * FROM `Product` where `enable`=1 and productID > 0 ORDER BY name ASC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Product> productlist = new LinkedList<Product>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productID = result.getInt(0);
			String name = result.getString(1);
			String productPhoto = result.getString(2);
			String price = result.getString(3);
			int productCategoryID = result.getInt(4);
			int enable = result.getInt(5);
			int syncFlag = result.getInt(6);
			Product p = new Product(productID, productCategoryID, name, price, productPhoto, enable);
			p.setSyncFlag(syncFlag);
			productlist.add(p);
		}
		result.close();

		return productlist;
	}

	public static List<Product> getALLProductForBackup(SQLiteDatabase sqlitedb) {
		String sql = "SELECT * FROM `Product` where `enable`=1";
		Cursor result = sqlitedb.rawQuery(sql, null);
		List<Product> productlist = new LinkedList<Product>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productID = result.getInt(0);
			String name = result.getString(1);
			String productPhoto = result.getString(2);
			String price = result.getString(3);
			int productCategoryID = result.getInt(4);
			int enable = result.getInt(5);
			int syncFlag = result.getInt(6);
			Product p = new Product(productID, productCategoryID, name, price, productPhoto, enable);
			p.setSyncFlag(syncFlag);
			productlist.add(p);
		}
		result.close();

		return productlist;
	}

	public static List<Product> getProductForSync() {
		String sql = "SELECT * FROM 'Product' where productID != 0 and syncFlag in (0,1) ORDER BY `productID` ASC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Product> productlist = new LinkedList<Product>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productID = result.getInt(0);
			String name = result.getString(1);
			String productPhoto = result.getString(2);
			String price = result.getString(3);
			int productCategoryID = result.getInt(4);
			int enable = result.getInt(5);
			int syncFlag = result.getInt(6);
			Product p = new Product(productID, productCategoryID, name, price, productPhoto, enable);
			p.setSyncFlag(syncFlag);
			productlist.add(p);
		}
		result.close();

		return productlist;
	}

	public static List<Onsale> getOnsaleForSync() {
		String sql = "SELECT * FROM 'Onsale' where syncFlag in (0,1) ORDER BY `onsaleID` ASC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Onsale> onsale = new LinkedList<Onsale>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int onsaleID = result.getInt(0);
			int onsaleType = result.getInt(1);
			String onsaleName = result.getString(2);
			String value = result.getString(3);
			int enable = result.getInt(4);
			int syncFlag = result.getInt(5);
			Onsale p = new Onsale(onsaleID, onsaleType, onsaleName, value, enable);
			p.setSyncFlag(syncFlag);
			onsale.add(p);
		}
		result.close();

		return onsale;
	}

	public static Onsale getOnSaleByid(int id) {
		String sql = "SELECT * FROM `Onsale` where onsaleID =" + id;
		Cursor result = msqlitedb.rawQuery(sql, null);

		if (!result.moveToFirst()) {
			result.close();
			return null;
		}

		Onsale mOnsale = new Onsale(result.getInt(0), result.getInt(1), result.getString(2), result.getString(3),
				result.getInt(4));
		return mOnsale;
	}

	public static List<Product> getALLProductOnSale() {
		String sql = "SELECT * FROM `Product` where `enable`=1 ORDER BY productID ASC";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Product> productlist = new LinkedList<Product>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productID = result.getInt(0);
			String name = result.getString(1);
			String productPhoto = result.getString(2);
			String price = result.getString(3);
			int productCategoryID = result.getInt(4);
			int enable = result.getInt(5);
			int syncFlag = result.getInt(6);
			Product p = new Product(productID, productCategoryID, name, price, productPhoto, enable);
			p.setSyncFlag(syncFlag);
			productlist.add(p);
		}
		result.close();

		return productlist;
	}

	/**
	 * 根据商品id返回商品信息
	 * 
	 * @param productId
	 * @return
	 */
	public static Product getProductByProductId(int productId) {
		return getProductByProductId(productId, true);
	}

	public static Product getProductByProductId(int productId, boolean benable) {

		String sql = null;
		if (benable)
			sql = "SELECT * FROM `Product` WHERE `enable`=1 and productID = " + String.valueOf(productId);
		else
			sql = "SELECT * FROM `Product` WHERE productID = " + String.valueOf(productId);

		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if (retval == false) {
			result.close();
			return null;
		}
		int productID = result.getInt(0);
		String name = result.getString(1);
		String productPhoto = result.getString(2);
		String price = result.getString(3);
		int productCategoryID = result.getInt(4);
		int enable = result.getInt(5);
		int syncFlag = result.getInt(6);
		Product p = new Product(productID, productCategoryID, name, price, productPhoto, enable);
		p.setSyncFlag(syncFlag);
		result.close();
		return p;
	}

	public static List<ProductPinyin> getProductPinyinList() {
		String sql = "select * from ProductPinyin where enable=1 order by firstChar asc";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<ProductPinyin> pinyinList = new ArrayList<ProductPinyin>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productID = result.getInt(0);
			int productCateID = result.getInt(1);
			String productname = result.getString(2);
			String firstchar = result.getString(3);

			ProductPinyin p = new ProductPinyin(productID, productCateID, productname, firstchar, null, null);
			pinyinList.add(p);
		}
		result.close();

		return pinyinList;
	}

	public static List<ProductPinyin> getProductPinyinListByCataID(int id) {
		String sql = "select * from ProductPinyin where enable = 1 and productCategoryID = " + id
				+ " order by upper(firstChar) asc";
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<ProductPinyin> pinyinList = new ArrayList<ProductPinyin>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productID = result.getInt(0);
			int productCateID = result.getInt(1);
			String productname = result.getString(2);
			String firstchar = result.getString(3);

			ProductPinyin p = new ProductPinyin(productID, productCateID, productname, firstchar, null, null);
			pinyinList.add(p);
		}
		result.close();

		return pinyinList;
	}

	public static void productCategorySynced(int id) {
		msqlitedb.execSQL("UPDATE `ProductCategory` SET syncFlag = 2 WHERE productCategoryID = " + id);
	}

	public static void productSynced(int id) {
		msqlitedb.execSQL("UPDATE `Product` SET syncFlag = 2 WHERE productID = " + id);
	}

	public static void productAttributeSynced(int id) {
		msqlitedb.execSQL("UPDATE `ProductAttribute` SET syncFlag = 2 WHERE productAttributeID = " + id);
	}

	public static void productSubAttributeSynced(int id) {
		msqlitedb.execSQL("UPDATE `ProductSubAttribute` SET syncFlag = 2 WHERE productSubAttributeID = " + id);
	}

	public static void onsaleSynced(int id) {
		msqlitedb.execSQL("UPDATE `Onsale` SET syncFlag = 2 WHERE onsaleID = " + id);
	}

	public static boolean createPinYinTable() {

		final String TABLE_PRODUCT_PINYIN = " create table  `ProductPinyin`  (`productId` INTEGER primary key , `productCategoryID` INTEGER ,"
				+ "`productName` VARCHAR, `firstChar` VARCHAR,`firstCharStr` VARCHAR, `allStr` VARCHAR, `enable` INTEGER )";
		msqlitedb.beginTransaction();
		boolean ret = false;
		try {
			msqlitedb.execSQL("DROP TABLE IF EXISTS `ProductPinyin`");
			msqlitedb.execSQL(TABLE_PRODUCT_PINYIN);
			msqlitedb.setTransactionSuccessful();
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msqlitedb.endTransaction();
		}
		return ret;
	}

	public static void InitPinyinTable() {
		createPinYinTable();

		msqlitedb.beginTransaction();
		try {
			List<Product> mprolist = getALLProduct();
			Iterator<Product> iterator = mprolist.iterator();
			while (iterator.hasNext()) {
				Product mc = iterator.next();
				ContentValues values = new ContentValues();

				int proid = mc.getProductID();
				values.put("productId", String.valueOf(proid));

				int cataid = mc.getProductCategoryID();
				values.put("productCategoryID", String.valueOf(cataid));

				String proname = mc.getName();
				values.put("productName", String.valueOf(proname));

				String firstChar = Common.getFirstPinYinHeadChar(proname);
				values.put("firstChar", String.valueOf(firstChar));

				values.put("enable", 1);

				msqlitedb.insertWithOnConflict("ProductPinyin", null, values, SQLiteDatabase.CONFLICT_IGNORE);
			}
			msqlitedb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msqlitedb.endTransaction();
		}
	}

	public static List<ProductToAttribute> getAllProductToAttributeForBackUp(SQLiteDatabase sqlitedb) {
		String sql = "SELECT * FROM `ProductToAttribute` where `enable`=1";
		Cursor result = sqlitedb.rawQuery(sql, null);
		List<ProductToAttribute> productlist = new LinkedList<ProductToAttribute>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int productID = result.getInt(0);
			int productAttributeID = result.getInt(1);
			ProductToAttribute p = new ProductToAttribute(productID, productAttributeID);
			productlist.add(p);
		}
		result.close();

		return productlist;
	}
}
