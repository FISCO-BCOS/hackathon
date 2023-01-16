# none/error/warning/debug/trace/all
GRAMINE_LOG_LEVEL = error

# directory with arch-specific libraries, used by Redis
# the below path works for Debian/Ubuntu; for CentOS/RHEL/Fedora, you should
# overwrite this default like this: `ARCH_LIBDIR=/lib64 make`
ARCH_LIBDIR ?= /lib/$(shell $(CC) -dumpmachine)

fisco-bcos.manifest: fisco-bcos.manifest.template
	gramine-manifest \
		-Dlog_level=$(GRAMINE_LOG_LEVEL) \
		-Darch_libdir=$(ARCH_LIBDIR) \
		$< > $@

sgx_outputs: fisco-bcos.manifest
	gramine-sgx-sign \
		--manifest fisco-bcos.manifest \
		--output fisco-bcos.manifest.sgx

fisco-bcos.token: fisco-bcos.sig
	gramine-sgx-get-token --output $@ --sig $<

all: sgx_outputs fisco-bcos.token
clean: rm *.token *.sig *.manifest.sgx *.manifest
