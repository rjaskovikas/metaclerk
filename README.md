# metaclerk
Database metadata comparison tool

#example

metaclerk snapshot -d mysql -c "jdbc:mysql://localhost?useSSL=false"  -u test -p test -s test | metaclerk.cmd check -d mysql -c "jdbc:mysql://localhost?useSSL=false" -u test -p test -s test -vv
