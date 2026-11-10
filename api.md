# 阅读·墨水屏 Content Provider API

本版已移除 Web 服务，仅保留 Android Content Provider 接口。

## Content Provider
* `providerHost` 为 `包名.readerProvider`，正式版是 `io.legado.ink.readerProvider`，调试版是 `io.legado.ink.debug.readerProvider`。
* 以下出现的`providerHost`请自行替换

#### 插入单个书源or订阅源

```
URL = content://providerHost/bookSource/insert
URL = content://providerHost/rssSource/insert
Method = insert
```

创建`Key="json"`的`ContentValues`，内容为`JSON`字符串，  
格式参考[这个文件](app/src/main/java/io/legado/app/data/entities/BookSource.kt)

#### 插入多个书源or订阅源

```
URL = content://providerHost/bookSources/insert
URL = content://providerHost/rssSources/insert
Method = insert
```

创建`Key="json"`的`ContentValues`，内容为`JSON`字符串，  
格式参考[这个文件](app/src/main/java/io/legado/app/data/entities/BookSource.kt)，**为数组格式**。

#### 获取书源or订阅源

```
URL = content://providerHost/bookSource/query?url=xxx
URL = content://providerHost/rssSource/query?url=xxx
Method = query
```

获取指定URL对应的书源信息。  
用`Cursor.getString(0)`取出返回结果。

#### 获取所有书源or订阅源

```
URL = content://providerHost/bookSources/query
URL = content://providerHost/rssSources/query
Method = query
```

获取APP内的所有书源。  
用`Cursor.getString(0)`取出返回结果。

#### 删除多个书源or订阅源

```
URL = content://providerHost/bookSources/delete
URL = content://providerHost/rssSources/delete
Method = delete
```

创建`Key="json"`的`ContentValues`，内容为`JSON`字符串，  
格式参考[这个文件](app/src/main/java/io/legado/app/data/entities/BookSource.kt)，**为数组格式**。

#### 插入书籍
```
URL = content://providerHost/book/insert
Method = insert
```

创建`Key="json"`的`ContentValues`，内容为`JSON`字符串，  
格式参考[这个文件](app/src/main/java/io/legado/app/data/entities/Book.kt)。

#### 获取所有书籍
```
URL = content://providerHost/books/query
Method = query
```

获取APP内的所有书籍。  
用`Cursor.getString(0)`取出返回结果。

#### 获取书籍章节列表
```
URL = content://providerHost/book/chapter/query?url=xxx
Method = query
```

获取指定图书的章节列表。   
用`Cursor.getString(0)`取出返回结果。

#### 获取书籍内容

```
URL = content://providerHost/book/content/query?url=xxx&index=1
Method = query
```
获取指定图书的第`index`章节的文本内容。     
用`Cursor.getString(0)`取出返回结果。

#### 获取封面
```
URL = content://providerHost/book/cover/query?path=xxxx
Method = query
```
