#!/bin/bash

tar -czvf nytools.tar.gz nytools.jar
echo `shasum -a 256 nytools.tar.gz`
