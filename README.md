# zoondka-maps
Zoondka Maps is a simple mapping web app that displays a map using internal tiles from the rest of our stack @ maps.zoondka.com. (see [zoondka/maps.zoondka.com](https://github.com/zoondka/maps.zoondka.com) for more details)

## Installing
```
git clone https://github.com/zoondka/zoondka-maps
```

## Building
Zoondka Maps uses the [Boot build framework](https://github.com/boot-clj/boot) for dev & production.

### Dev
With Boot installed, you can launch a [http-kit](https://github.com/http-kit/http-kit) server with Zoondka Maps served on http://localhost:8090 for use when developing:
```shell
cd zoondka-maps
boot dev
```
Changes to source are automatically recompiled.

### Production
For use in production, you can package the app & app server as an uberjar:
```shell
boot prod
```
Run the app like so:
```shell
/usr/bin/java -jar target/zoondka-maps.jar $PORT
```
Or install it as a systemd service:
```shell
mkdir /srv/zoondka-maps
cp target/zoondka-maps.jar /srv/zoondka-maps/
cp dist/zoondka-maps.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable zoondka-maps
sudo systemctl start zoondka-maps
```
