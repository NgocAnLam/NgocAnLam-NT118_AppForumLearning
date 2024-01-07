import mysql.connector
from mysql.connector import IntegrityError
import requests

mydb = mysql.connector.connect(
  host = "localhost",
  user = "root",
  password = "123456"
)

mycursor = mydb.cursor()

# Create a tempProduct save id and spid of the product ----------------------------------------------------------------

# mycursor.execute("DROP DATABASE mydatabase")
# mycursor.execute("CREATE DATABASE mydatabase")
# mycursor.execute('''
#     CREATE TABLE mydatabase.tempProduct (
#         id INT PRIMARY KEY,
#         spid INT
#     );
# ''')

# header = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36'}
# url = "https://tiki.vn/api/personalish/v1/blocks/listings?limit=40&include=advertisement&aggregations=2&version=home-persionalized&trackity_id=de393bda-e986-e33b-a68d-63a7d784ed06&category=1789&page={}&urlKey=dien-thoai-may-tinh-bang"

# for i in range(1, 21):
#     response = requests.get(url = url.format(i), headers = header)
#     if response.status_code == 200:
#         results = response.json() 
#         for product in results['data']:
#             try:
#                 mycursor.execute("INSERT INTO mydatabase.tempProduct (id, spid) VALUES ({},{});".format(product['id'], product['seller_product_id']))
#                 mydb.commit()
#             except IntegrityError as e:
#                 pass
    
# create detail product

header = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36'}
url = "https://tiki.vn/api/v2/products/{}?platform=web&spid={}&version=3"

response = requests.get(url = url.format('2360457', '17616570'), headers = header)
if response.status_code == 200:
    results = response.json()
    print(results)

# mycursor.execute('''
#     CREATE TABLE mydatabase.Product ( 
#         id INT PRIMARY KEY,
#         master_id VARCHAR(512),
#         sku VARCHAR(512),
#         name VARCHAR(512),
#         url_key VARCHAR(512),
#         url_path VARCHAR(512),
#         short_url VARCHAR(512),
#         type VARCHAR(512),
#         book_cover VARCHAR(512),
#         short_description VARCHAR(512),
#         price VARCHAR(512),
#         list_price VARCHAR(512),
#         original_price VARCHAR(512),
#         badges VARCHAR(512),
#         badges_new VARCHAR(512),
#         discount VARCHAR(512),
#         discount_rate VARCHAR(512),
#         rating_average VARCHAR(512),
#         review_count VARCHAR(512),
#         review_text VARCHAR(512),
#         favourite_count VARCHAR(512),
#         thumbnail_url VARCHAR(512),
#         has_ebook VARCHAR(512),
#         inventory_status VARCHAR(512),
#         inventory_type VARCHAR(512),
#         productset_group_name VARCHAR(512),
#         is_fresh VARCHAR(512),
#         seller VARCHAR(512),
#         is_flower VARCHAR(512),
#         has_buynow VARCHAR(512),
#         is_gift_card VARCHAR(512),
#         salable_type VARCHAR(512),
#         data_version VARCHAR(512),
#         day_ago_created VARCHAR(512),
#         all_time_quantity_sold VARCHAR(512),
#         meta_title VARCHAR(512),
#         meta_description VARCHAR(512),
#         meta_keywords VARCHAR(512),
#         is_baby_milk VARCHAR(512),
#         is_acoholic_drink VARCHAR(512),
#         description VARCHAR(512),
#         images VARCHAR(512),
#         warranty_policy VARCHAR(512),
#         brand VARCHAR(512),
#         current_seller VARCHAR(512),
#         other_sellers VARCHAR(512),
#         specifications VARCHAR(512),
#         product_links VARCHAR(512),
#         gift_item_title VARCHAR(512),
#         services_and_promotions VARCHAR(512),
#         promitions VARCHAR(512),
#         stock_item VARCHAR(512),
#         quantity_sold VARCHAR(512),
#         categories VARCHAR(512),
#         breadcrumbs VARCHAR(512),
#         installment_info_v2 VARCHAR(512),
#         installment_info_v3 VARCHAR(512),
#         is_seller_in_chat_whitelist VARCHAR(512),
#         inventory VARCHAR(512),
#         warranty_info VARCHAR(512),
#         return_and_exchange_policy VARCHAR(512),
#         is_tier_pricing_available VARCHAR(512),
#         is_tier_pricing_eligible VARCHAR(512),
#         benefits VARCHAR(512)
#     );
# ''')
