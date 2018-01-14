from bs4 import BeautifulSoup
import requests


# jobs.ps has changed it's website design so this code may not be valid now

data = requests.get("http://www.jobs.ps/index.php")
bytes = data.content
soup2 = BeautifulSoup(bytes,"html.parser")

main_window_for_jobs = soup2.find(id="tabs-1")

for link in main_window_for_jobs.find_all("a",{'class':"cat_parent_link"}):
    web_page = link['href']
    data = requests.get(web_page)
    bytes = data.content
    soup = BeautifulSoup(bytes, "html.parser")

    green_page = soup.find_all("tr",{'class':'green_grad'})
    blue_page = soup.find_all("tr",{'class':'blue_grad'})

    for item,blue_item in zip(green_page,blue_page):
        the_date = item.find_all("td")[0].text
        job_title = item.find_all("td")[1].find("a",{'class':'job_list_title_premium'}).text
        job_type = item.find_all("td")[1].find("span",{'class':'job_list_cat_name_premium'}).text
        job_location = item.find_all("td")[2].find("b").text

        the_date2 = blue_item.find_all("td")[0].text
        job_title2 = blue_item.find_all("td")[1].find("a",{'class':'job_list_title_premium'}).text
        job_type2 = blue_item.find_all("td")[1].find("span",{'class':'job_list_cat_name_premium'}).text
        job_location2 = blue_item.find_all("td")[2].find("b").text
        print('-------GREEN START')
        print(the_date)
        print(job_title)
        print(job_type)
        print(job_location)
        print('-------GREEN END')

        print('-------BLUE START')
        print(the_date2)
        print(job_title2)
        print(job_type2)
        print(job_location2)
        print('-------BLUE END')
