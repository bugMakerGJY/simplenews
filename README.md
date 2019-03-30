# simplenews
news-recommendation system

# 2019.1.21
从中国新闻网（www.chinanews.com)  爬取了数据。  
返回数据类型  
  
news_content = {  
                'title':标题  
                'tag': 类型标签  
                'article': 新闻内容  
                'time':发布时间  
                'source': 来源  
                'pics_list': 图片列表  
            }  

# 2019.1.22
调整了数据格式，添加了将数据存储mysql的代码

# 2019.3.8
用Springboot实现了新闻的crud，分页功能

# 2019.3.30 
增加了各种接口，完成了前端页面，项目部署阿里云
