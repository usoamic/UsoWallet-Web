# UsoWallet-Web
### Know issues:
Q: What if
```shell script
npm WARN deprecated phantomjs-prebuilt@2.1.16: this package is now deprecated
npm ERR! code 128
npm ERR! Command failed: git submodule update -q --init --recursive
npm ERR! fatal: 'submodule' appears to be a git command, but we were not
npm ERR! able to execute it. Maybe git-submodule is broken?
npm ERR! 
```

A: 
```shell script
cd build
npm install web3@v1.2.2
```
and restart building...