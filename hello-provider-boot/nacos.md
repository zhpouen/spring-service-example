在nacos配置管理，配置一个dataId为`hello-provider-boot-flow-rules`的配置，内容如下：
```json
[
  {
    "resource": "/echo/{string}",
    "grade": 1,
    "count": 10
  },
  {
    "resource": "/divide",
    "grade": 1,
    "count": 5
  }
]
```