import vars.multiarch

def images = [
	'alpine',
	'buildpack',
	'busybox',
	'debian',
	'drupal',
	'erlang',
	'fedora',
	'gcc',
	'golang',
	'haproxy',
	'hello',
	'httpd',
	'irssi',
	'maven',
	'memcached',
	'neo4j',
	'node',
	'openjdk',
	'opensuse',
	'perl',
	'php',
	'postgres',
	'pypy',
	'python',
	'r',
	'rakudo',
	'redis',
	'ruby',
	'solr',
	'tomcat',
	'ubuntu',
	'wordpress',
]

def myView(viewFunc, viewName, viewRegex) {
	viewFunc(viewName) {
		//filterBuildQueue()
		filterExecutors()
		jobs {
			regex(viewRegex)
		}
		columns {
			status()
			weather()
			name()
			lastDuration()
			lastSuccess()
			lastFailure()
			buildButton()
		}
	}
}

nestedView('docker-multiarch') {
	description('BECAUSE IBM')
	filterBuildQueue()
	filterExecutors()
	columns {
		status()
		weather()
	}
	views {
		myView(delegate.&listView, '-all', 'docker-.*')
		myView(delegate.&listView, '-docs', 'docker-.*-docs')
		myView(delegate.&listView, '-meta', 'docker-multiarch-.*')
		nestedView('arches') {
			filterBuildQueue()
			filterExecutors()
			columns {
				status()
				weather()
			}
			views {
				myView(delegate.&listView, '-all', 'docker-.*')
				for (arch in multiarch.allArches()) {
					myView(delegate.&listView, arch, 'docker-' + arch + '-.*')
				}
			}
		}
		nestedView('images') {
			filterBuildQueue()
			filterExecutors()
			columns {
				status()
				weather()
			}
			views {
				myView(delegate.&listView, '-all', 'docker-.*')
				myView(delegate.&listView, '-docs', 'docker-.*-docs')
				for (image in images) {
					myView(delegate.&listView, image, 'docker-.*-' + image)
				}
			}
		}
	}
}
