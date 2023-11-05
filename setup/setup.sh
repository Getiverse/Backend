#!/bin/bash
cat getiverse.categories.json | mongoimport --collection='categories'
cat getiverse.instants.json | mongoimport --collection='instants'
cat getiverse.library.json | mongoimport --collection='library'
cat getiverse.posts.json | mongoimport --collection='posts'
cat getiverse.users.json | mongoimport --collection='users'