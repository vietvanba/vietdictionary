import sys
import requests
from bs4 import BeautifulSoup
import json

if len(sys.argv) != 2:
    print("Sử dụng: python file.py <URL>")
    sys.exit(1)

url = 'http://www.oxfordlearnersdictionaries.com/definition/english/'
url += sys.argv[1]

headers = requests.utils.default_headers()
headers.update(
    {
        'User-Agent': 'PostmanRuntime/7.33.0',
    }
)
response = requests.get(url, headers=headers)
data_list = {}  # Danh sách để lưu các dictionary dữ liệu

if response.status_code == 200:
    soup = BeautifulSoup(response.text, 'html.parser')

    divs = soup.find_all('div', class_='entry')
    for div in divs:
        word = div.find(class_="headword").text

        phon_br_div = div.find('div', class_='phons_br')
        phon_br_mp3 = phon_br_div.find(class_='sound audio_play_button pron-uk icon-audio')['data-src-mp3']
        phon_br_text = phon_br_div.find(class_='phon').text

        phon_am_div = div.find('div', class_='phons_n_am')
        phon_am_mp3 = phon_am_div.find(class_='sound audio_play_button pron-us icon-audio')['data-src-mp3']
        phon_am_text = phon_am_div.find(class_='phon').text

        entry_data = {
            "word": word,
            "phon": {
                "us": {
                    "mp3": phon_br_mp3,
                    "pronunciation": phon_br_text
                },
                "uk": {
                    "mp3": phon_am_mp3,
                    "pronunciation": phon_am_text
                }
            }
        }

        shcuts = soup.find_all('span', class_='shcut-g')
        if len(shcuts) == 0:
            shcut_data = []
            senses_multiple = soup.find_all('li', class_='sense')
            for sense in senses_multiple:
                detail = sense.find('span', class_='def')
                examples = sense.find_all('span', class_='x')
                shcut_data.append(
                    {
                        "shortcut":None,
                        "sense": detail.text,
                        "examples": [example.text for example in examples]
                    }
                )
            entry_data["senses"] = shcut_data
        else:
            shcut_data = []
            for shcut in shcuts:
                shcut_text = shcut.find('h2', class_='shcut').text
                detail = shcut.find('span', class_='def')
                examples = shcut.find_all('span', class_='x')
                shcut_data.append(
                    {
                        "shortcut": shcut_text,
                        "sense": detail.text,
                        "examples": [example.text for example in examples]
                    }
                )
            entry_data["senses"] = shcut_data

        data_list=entry_data
    json_data = json.dumps(data_list, indent=4)

    print(json_data)
else:
    print("Không thể tải trang web. Mã lỗi:", response.status_code)
