package com.sp62b21.myapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sp62b21.myapp.data.models.Brand
import com.sp62b21.myapp.data.models.Category
import com.sp62b21.myapp.data.models.PAO
import com.sp62b21.myapp.data.models.Product
import java.util.concurrent.Executors


//@TypeConverters(DateTypeConverter::class)
@Database(entities = [Product::class, PAO::class, Brand::class, Category::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun paoDao(): PAODao
    abstract fun brandDao(): BrandDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(AppDatabase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context).also { INSTANCE = it }
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "myapp_db")
                // prepopulate the database after onCreate was called
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // insert the data on the IO Thread
                        ioThread {
                            getInstance(context).paoDao().insertAll(PREPOPULATE_PAO_DATA)
                            getInstance(context).brandDao().insertAll(PREPOPULATE_BRAND_DATA)
                            getInstance(context).categoryDao().insertAll(PREPOPULATE_CATEGORY_DATA)
                        }
                    }
                })
                .allowMainThreadQueries()
                .build()
        
        val PREPOPULATE_PAO_DATA = listOf(
            PAO(1,"1M"), 
            PAO(2,"2M"),
            PAO(3,"3M"),
            PAO(4,"4M"),
            PAO(5,"6M"),
            PAO(6,"9M"),
            PAO(7,"12M"),
            PAO(8,"15M"),
            PAO(9,"18M"),
            PAO(10,"20M"),
            PAO(11,"24M"),
            PAO(12,"30M"),
            PAO(13,"36M"),
            PAO(14,"48M"),
            PAO(15,"∞"))

        val PREPOPULATE_BRAND_DATA = listOf(
            Brand(1,"Test1"),
            Brand(2,"Test2"),
            Brand(3,"Test3"),
            Brand(4,"Test4"),
            Brand(5,"Test5"),
            Brand(6,"Albion Co., Ltd."),
            Brand(7,"Almay"),
            Brand(8,"Aqua Net"),
            Brand(9,"Avon Products"),
            Brand(10,"Bain de Soleil"),
            Brand(11,"Bajaj Consumer Care"),
            Brand(12,"Balmshell"),
            Brand(13,"Bayankala "),
            Brand(14,"Beautycounter"),
            Brand(15,"Benefit Cosmetics"),
            Brand(16,"Biotherm"),
            Brand(17,"BITE Beauty"),
            Brand(18,"Bondi Sands"),
            Brand(19,"Boots "),
            Brand(20,"O Boticário"),
            Brand(21,"Bobbi Brown"),
            Brand(22,"Burt's Bees"),
            Brand(23,"Cativa Natureza"),
            Brand(24,"Chanel"),
            Brand(25,"Clarins"),
            Brand(26,"Clinique"),
            Brand(27,"ColourPop Cosmetics"),
            Brand(28,"Coppertone "),
            Brand(29,"CoverGirl"),
            Brand(30,"Crème Simon"),
            Brand(31,"Cristtee"),
            Brand(32,"DHC Corporation"),
            Brand(33,"Douglas Holding"),
            Brand(34,"Elf "),
            Brand(35,"Elizabeth Arden, Inc."),
            Brand(36,"Etude House"),
            Brand(37,"Fabergé "),
            Brand(38,"Fair & Lovely "),
            Brand(39,"Fenty Beauty"),
            Brand(40,"Forest Essentials"),
            Brand(41,"Forever Living Products"),
            Brand(42,"Fourth Ray Beauty"),
            Brand(43,"Hada Labo"),
            Brand(44,"Halo Beauty"),
            Brand(45,"Haus Laboratories"),
            Brand(46,"Hermès"),
            Brand(47,"Holo Taco"),
            Brand(48,"Huda Beauty"),
            Brand(49,"Imedeen"),
            Brand(50,"Inglot Cosmetics"),
            Brand(51,"Inoherb"),
            Brand(52,"IsaDora cosmetics"),
            Brand(53,"Jaclyn Hill Cosmetics"),
            Brand(54,"Jeffree Star Cosmetics"),
            Brand(55,"Jo Malone London"),
            Brand(56,"Kiehl's"),
            Brand(57,"Kim Chi Chic Beauty"),
            Brand(58,"KKW Beauty"),
            Brand(59,"Kylie Cosmetics"),
            Brand(60,"Lakmé Cosmetics"),
            Brand(61,"Lancôme"),
            Brand(62,"Laura Mercier Cosmetics"),
            Brand(63,"Lime Crime"),
            Brand(64,"Lubriderm"),
            Brand(65,"Lunar Beauty"),
            Brand(66,"MAC Cosmetics"),
            Brand(67,"Madara Cosmetics"),
            Brand(68,"Madison Reed"),
            Brand(69,"Mandom"),
            Brand(70,"NARS Cosmetics"),
            Brand(71,"Natura & Co"),
            Brand(72,"Natural Wonder "),
            Brand(73,"Neutrogena"),
            Brand(74,"No. 7 "),
            Brand(75,"NYX Professional Makeup"),
            Brand(76,"OPI Products"),
            Brand(77,"Origins "),
            Brand(78,"Parachute "),
            Brand(79,"Pechoin"),
            Brand(80,"Princess Pat "),
            Brand(81,"Rimmel"),
            Brand(82,"Sa Sa International Holdings"),
            Brand(83,"Schwan-Stabilo"),
            Brand(84,"Shanghai Vive"),
            Brand(85,"Shiseido"),
            Brand(86,"SK-II"),
            Brand(87,"Sol Body"),
            Brand(88,"Space.NK"),
            Brand(89,"St. Tropez "),
            Brand(90,"Stratton "),
            Brand(91,"Surya Brasil"),
            Brand(92,"Tarte Cosmetics"),
            Brand(93,"Tati Beauty"),
            Brand(94,"Trixie Cosmetics"),
            Brand(95,"Tropic Skincare"),
            Brand(96,"Ultima II "),
            Brand(97,"Urban Decay "),
            Brand(98,"Yves Saint Laurent ")

            )

        val PREPOPULATE_CATEGORY_DATA = listOf(
            Category(1,"Test1","Group1")
        )

        private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

        /**
         * Utility method to run blocks on a dedicated background thread, used for io/database work.
         */
        fun ioThread(f : () -> Unit) {
            IO_EXECUTOR.execute(f)
        }

//        private class AppDatabaseCallback() : RoomDatabase.Callback() {
//            /**
//             * Override the onOpen method to populate the database.
//             * For this sample, we clear the database every time it is created or opened.
//             */
//            override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
//                super.onCreate(db)
////                Executors.newSingleThreadScheduledExecutor().execute(Runnable {
////                    getInstance(this@AppDatabase).dataDao().insertAll(DataEntity.populateData())
////                })
//                ioThread {
//                    getInstance(context).dataDao().insertData(PREPOPULATE_DATA)
//                }
//                populatePAOData(database.paoDao())
//                populateBrandData(database.brandDao())
//            }
//
//            override fun onOpen(db: SupportSQLiteDatabase) {
//                super.onOpen(db)
//                // If you want to keep the data through app restarts,
//                // comment out the following line.
//                INSTANCE?.let { database ->
//                    scope.launch(Dispatchers.IO) {
//                        populatePAOData(database.paoDao())
//                        populateBrandData(database.brandDao())
//                    }
//                }
//            }
//        }





        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
         fun populatePAOData(paoDao: PAODao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            paoDao.deleteAllPAOs()
            paoDao.insertPAO(PAO(1,"1M"))
            paoDao.insertPAO(PAO(2,"2M"))
            paoDao.insertPAO(PAO(3,"3M"))
            paoDao.insertPAO(PAO(4,"4M"))
            paoDao.insertPAO(PAO(5,"6M"))
            paoDao.insertPAO(PAO(6,"9M"))
            paoDao.insertPAO(PAO(7,"12M"))
            paoDao.insertPAO(PAO(8,"15M"))
            paoDao.insertPAO(PAO(9,"18M"))
            paoDao.insertPAO(PAO(10,"20M"))
            paoDao.insertPAO(PAO(11,"24M"))
            paoDao.insertPAO(PAO(12,"30M"))
            paoDao.insertPAO(PAO(13,"36M"))
            paoDao.insertPAO(PAO(14,"48M"))
            paoDao.insertPAO(PAO(15,"∞"))
        }

        fun populateBrandData(brandDao: BrandDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            brandDao.deleteAllBrands()
            brandDao.saveBrand(Brand(1,"Test1"))
            brandDao.saveBrand(Brand(2,"Test2"))
            brandDao.saveBrand(Brand(3,"Test3"))
            brandDao.saveBrand(Brand(4,"Test4"))
            brandDao.saveBrand(Brand(5,"Test5"))
        }

        suspend fun populateCategoryData(categoryDao: CategoryDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            categoryDao.deleteAllCategories()
            categoryDao.saveCategory(Category(0,"Test","Test"))
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }


}

