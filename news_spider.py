# -*- coding: utf-8 -*-
from pyquery import PyQuery as pq
import requests
import re
import pymysql

# 打开数据库连接
db = pymysql.connect("101.132.42.71", "news361", "970706", "news361")
first_page_url = 'http://www.chinanews.com/scroll-news/news1.html'


# 获取新闻类型标签
def get_new_tags(url):
    content = requests.get(url)
    content.encoding = 'GBK'
    doc = pq(content.text)
    results = doc('#newsdh > a:not(:first-child)')
    tag_list = []
    for result in results.items():
        url = 'http:'+result.attr('href')
        name = result.text()
        tag = {
            'tag_url': url,
            'tag_name': name
        }
        tag_list.append(tag)
    tag_list.pop(0)
    tag_list.pop()
    tag_list.pop()
    tag_list.pop()
    tag_list.pop()
    return tag_list


# 获取新闻列表
def get_news_list(tag):
    try:
        content = requests.get(tag['tag_url'])
        content.encoding = 'GBK'
        doc = pq(content.text)
        results = doc('div.dd_bt > a')
        news_list = []
        for result in results.items():
            url = result.attr('href')
            title = result.text()
            if url is not None and title != '':
                news = {
                    'news_id': re.findall("\d+", url)[3],
                    'news_url': 'http://www.chinanews.com'+url,
                    'news_title': title,
                    'news_tag': tag['tag_name']
                }
                news_list.append(news)
        return news_list
    except Exception:
        return None


# 获取新闻内容
def get_news(news):
    try:
        content = requests.get(news['news_url'])
        content.encoding = 'GBK'
        doc = pq(content.text)
        article_ps = doc('#cont_1_1_2 > div.left_zw > p')  # 获取新闻内容的p标签
        article = ''
        for p in article_ps.items():
            article += p.text()+'\n'
        pics_results = doc('div.left_zw > div[style="text-align:center"] > img')  # 获取img标签
        pics_list = []
        for result in pics_results.items():
            pic_title = result.attr('title')
            pic_src = result.attr('src')
            if pic_title is not None and pic_src is not None:
                pic = {
                    'pic_title': re.sub(r'<(S*?)[^>]*>.*?|<.*? />', '', pic_title),  # 去除标题里包涵的html
                    'pic_src': pic_src
                }
                pics_list.append(pic)
        time = doc('#pubtime_baidu').text()
        source = doc('#source_baidu').text()
        if article != '' and time != '' and source != '' and pics_list is not None:
            news_content = {
                'id': news['news_id'],
                'title': news['news_title'],
                'tag': news['news_tag'],
                'article': article,
                'time': time,
                'source': source,
                'pics_list': pics_list
            }
            return news_content
    except Exception:
        return None


def insert_into_news(news):
    # 使用cursor()方法获取操作游标
    cursor = db.cursor()

    # SQL 插入语句
    sql = """INSERT INTO news(news_id,
             title, tag, article, source, time)
             VALUES (
             """ + news['id'] + ',' + '\''+news['title']+'\'' + ','+'\''+news['tag']+'\''+',' + '\''+news['article']+'\''+',' + '\''+news['source']+'\''+',' + '\''+news['time']+'\')'
    try:
        # 执行sql语句
        cursor.execute(sql)
        # 提交到数据库执行
        db.commit()
        return True
    except Exception:
        # 如果发生错误则回滚
        print('sql执行错误')
        # print(sql)
        db.rollback()
        return False


def insert_into_pic(id, pic):
    # 使用cursor()方法获取操作游标
    cursor = db.cursor()

    # SQL 插入语句
    sql = """INSERT INTO pic(news_id,
             title, src)
             VALUES (
             """ + id + ',' + '\'' + pic['pic_title']+'\'' + ','+'\'' + pic['pic_src']+'\''+')'
    try:
        # 执行sql语句
        cursor.execute(sql)
        # 提交到数据库执行
        db.commit()
        return True
    except Exception:
        # 如果发生错误则回滚
        print('sql执行错误')
        # print(sql)
        db.rollback()
        return False


def insert_into_tag(tag):
    # 使用cursor()方法获取操作游标
    cursor = db.cursor()

    # SQL 插入语句
    sql = """INSERT INTO tag(name)
                VALUES (
                """ + '\'' + tag + '\'' + ')'
    try:
        # 执行sql语句
        cursor.execute(sql)
        # 提交到数据库执行
        db.commit()
        return True
    except Exception:
        # 如果发生错误则回滚
        print('sql执行错误')
        # print(sql)
        db.rollback()
        return False


tag_list = get_new_tags(first_page_url)
for tag in tag_list:
    news_list = get_news_list(tag)
    insert_into_tag(tag["tag_name"])
    if news_list is not None:
        for news in news_list:
            contents = get_news(news)
            if contents is not None:
                print(get_news(news))
                if insert_into_news(contents):
                    for pic in contents['pics_list']:
                        insert_into_pic(contents['id'],pic)


# 关闭数据库连接
db.close()