var authToken;
$(document).ready(function() {
    authToken = getUrlParameter("token");
    var scale = 2;
    var pdfData;

    $(window).load(function() {

        $('#intimation-dialog').dialog({
            modal: true,
            autoOpen: false,
            width: '85%',
            resizable: true,
            title: 'View Intimation Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $('#query_details_modal').dialog({
            modal: true,
            autoOpen: false,
            width: '85%',
            resizable: true,
            title: 'View Query Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#hospitals-details-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: '85%',
            resizable: true,
            title: 'Hospital Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#copay-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: '50%',
            resizable: true,
            title: 'Co-pay Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#fvr-detail-modal").dialog({
            modal: true,
            autoOpen: false,
            width: '85%',
            resizable: true,
            title: 'FVR Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#claim_history_modal").dialog({
            modal: true,
            autoOpen: false,
            width: '85%',
            resizable: true,
            title: 'History'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#doctor-trails-modal").dialog({
            modal: true,
            autoOpen: false,
            width: '85%',
            resizable: true,
            title: 'Doctor Note (Trails)'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#PED-request-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "95%",
            resizable: true,
            title: 'PED Request Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        /*$("#dialog-modal-thirteen").dialog({
        	modal: true,
        	autoOpen: false,
        	width: 'auto',
        	resizable: true
        });*/
        $("#preauth-details-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true,
            title: 'Pre-Auth Details',
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#acknowledge-details-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true,
            title: 'View Acknowledgement Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#coordinator-reply-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true
        }).prev(".ui-dialog-titlebar").css("background", "#003366");

        $("#specialist-trail-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true
        }).prev(".ui-dialog-titlebar").css("background", "#003366");

        $("#sublimits-details-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true
        }).prev(".ui-dialog-titlebar").css("background", "#003366");

        $("#hrm-details-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true
        }).prev(".ui-dialog-titlebar").css("background", "#003366");

        $("#risk-details-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true
        }).prev(".ui-dialog-titlebar").css("background", "#003366");

        $("#previous-insurance-details-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true
        }).prev(".ui-dialog-titlebar").css("background", "#003366");

        $("#balance-sum-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true
        }).prev(".ui-dialog-titlebar").css("background", "#003366");

        $("#top-panel-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true,
            title: 'Top Panel Elements'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White",
            "font-size": "12px"
        });

        $("#irda-non-payables-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true,
            title: 'IRDA Non-Payable List'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White",
            "font-size": "12px"
        });

        $("#irda-bill-details-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true,
            title: 'View Category Wise Bill Details (IRDA )'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White",
            "font-size": "12px"
        });

        $("#64vb_complaince-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true,
            title: '64 VB Compliance Report'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White",
            "font-size": "12px"
        });

        $("#product-benefits-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true,
            title: 'Product Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#current_policy-details-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true,
            title: 'View Policy Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#bill-summary-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true,
            title: 'View Bill Summary'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $('#translation-misc-modal').dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true,
            title: 'View Translation / Misc Request'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#previous-claim-details-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "80%",
            resizable: true,
            title: 'Previous Claim Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#earlier-ack-detail-modal").dialog({
            modal: true,
            autoOpen: false,
            width: '85%',
            resizable: true,
            title: 'Earlier Acknowlegment Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $("#investigation-request-dialog").dialog({
            modal: true,
            autoOpen: false,
            width: "95%",
            resizable: true,
            title: 'Investigation Details'
        }).prev(".ui-dialog-titlebar").css({
            "background": "#003366",
            "color": "White"
        });

        $('#gobutton').click(function() {
            if ($('#selectbox').val() == "intimation") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "intimation"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            if (data.intimation) {
                                var intimation = data.intimation;
                                $('#intNo').html((intimation.intimationNo ? intimation.intimationNo : ""));
                                $('#intPolNo').html((intimation.policyNo ? intimation.policyNo : ""));
                                $('#intDate').html((intimation.dateAndTime ? intimation.dateAndTime : ""));
                                $('#intIssueOffice').html((intimation.issueOffice ? intimation.issueOffice : ""));
                                $('#intCpuCode').html((intimation.cpuCode ? intimation.cpuCode : ""));
                                $('#intProductName').html((intimation.productName ? intimation.productName : ""));
                                $('#intMode').html((intimation.intimationMode ? intimation.intimationMode : ""));
                                $('#intProposerName').html((intimation.proposerName ? intimation.proposerName : ""));
                                $('#intIntimatedBy').html((intimation.intimatedBy ? intimation.intimatedBy : ""));
                                $('#intState').html((intimation.state ? intimation.state : ""));
                                $('#intPatientName').html((intimation.patientName ? intimation.patientName : ""));
                                $('#intCity').html((intimation.city ? intimation.city : ""));
                                $('#intIsCovered').html((intimation.isPatientCovered == true ? '<input disabled type="checkbox" checked />Patient Not Covered' : '<input disabled type="checkbox"  />Patient Not Covered'));
                                $('#intArea').html((intimation.area ? intimation.area : ""));
                                $('#intHealthCardNo').html((intimation.healthCardNo ? intimation.healthCardNo : ""));
                                $('#intHospName').html((intimation.hospName ? intimation.hospName : ""));
                                $('#intName').html((intimation.name ? intimation.name : ""));
                                $('#intAddress').html((intimation.address ? intimation.address : ""));
                                $('#intRelation').html((intimation.relationship ? intimation.relationship : ""));
                                $('#intAdDate').html((intimation.admissionDate ? intimation.admissionDate : ""));
                                $('#intAdType').html((intimation.admissionType ? intimation.admissionType : ""));
                                $('#intInpatient').html((intimation.inpatient ? intimation.inpatient : ""));
                                $('#intHosptype').html((intimation.hospType ? intimation.hospType : ""));
                                $('#intLateIntimation').html((intimation.lateIntimation ? intimation.lateIntimation : ""));
                                $('#intHospCode').html((intimation.hospCode ? intimation.hospCode : ""));
                                $('#intAdReason').html((intimation.admissionReason ? intimation.admissionReason : ""));
                                $('#intHospCodeIrda').html((intimation.irdaHospCode ? intimation.irdaHospCode : ""));
                                $('#intComments').html((intimation.comments ? intimation.comments : ""));
                                $('#intSmCode').html((intimation.smCode ? intimation.smCode : ""));
                                $('#intSmName').html((intimation.smName ? intimation.smName : ""));
                                $('#intBrokerCode').html((intimation.brokerCode ? intimation.brokerCode : ""));
                                $('#intBrokerName').html((intimation.brokerName ? intimation.brokerName : ""));
                            }
                            $('#intimation-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            $('#intimation-dialog').dialog('open');
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "queryDetails") {
                $('#query_details_modal').dialog('open');
            }

            if ($('#selectbox').val() == "policySchedule") {
                var url = $('#getPolicyUrl').val();
                if (url) {
                    window.open(url, "popUp", "resizable=yes");
                }
            }

            if ($('#selectbox').val() == "merDetails") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "mer_details"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            if (data.medReportUrl) {
                                window.open(data.medReportUrl, "popUp", "resizable=yes");
                            }
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "policySchedule") {
                var url = $('#getPolicyUrl').val();
                if (url) {
                    window.open(url, "popUp", "resizable=yes");
                }
            }

            if ($('#selectbox').val() == "hospDetails") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "hospitals"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('#hosp_name').html((data.hospName ? data.hospName : ""));
                            $('#hosp_code').html((data.hospCode ? data.hospCode : ""));
                            $('#hosp_phone').html((data.hospPhoneNo ? data.hospPhoneNo : ""));
                            $('#hosp_address').html((data.address ? data.address : ""));
                            $('#hosp_rep').html((data.authRep ? data.authRep : ""));
                            $('#hosp_city').html((data.city ? data.city : ""));
                            $('#hosp_rep_name').html((data.repName ? data.repName : ""));
                            $('#hosp_state').html((data.state ? data.state : ""));
                            $('#hosp_category').html((data.hospCategory ? data.hospCategory : ""));
                            $('#hosp_pin').html((data.pincode ? data.pincode : ""));
                            $('#hosp_room_category').html((data.roomCategory ? data.roomCategory : ""));
                            $('#hosp_package_url').html((data.packageUrl ? '<a href="' + data.packageUrl + '" class="hreflink" target="_blank"> View Package Rates </a>' : ""));
                            $('#hosp_remarks').html((data.remarks ? data.remarks : ""));
                            $("table.riskDetailsTable tr:even").css("background-color", "#c5e0dc");
                            $('#hospitals-details-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            $('#hospitals-details-dialog').dialog('open');
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "copayDetails") {
                $("table.riskDetailsTable tr:even").css("background-color", "#c5e0dc");
                $('#copay-dialog').dialog('open');
            }
            if ($('#selectbox').val() == "fvrDetails") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "fvr_details"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.fvr-details-tbody').html('');
                            if (data.fvrDetails) {
                                var fvrDetails = data.fvrDetails;
                                for (var i = 0; i < fvrDetails.length; i++) {
                                    $('.fvr-details-tbody').append('<tr id="fvr_' + (i + 1) + '" data-grading="" data-json="" 	><td>' + (i + 1) + '</td><td>' +
                                        (fvrDetails[i].representativeName ? fvrDetails[i].representativeName : '') + '</td><td>' + (fvrDetails[i].representativeCode ? fvrDetails[i].representativeCode : '') + '</td><td>' + (fvrDetails[i].representativeContactNo ? fvrDetails[i].representativeContactNo : '') + '</td><td>' + (fvrDetails[i].hospitalName ? fvrDetails[i].hospitalName : '') + '</td><td>' +
                                        (fvrDetails[i].hospitalVisitedDate ? fvrDetails[i].hospitalVisitedDate : '') + '</td><td>' + (fvrDetails[i].remarks ? fvrDetails[i].remarks : '') + '</td><td>' + (fvrDetails[i].fvrassignedDate ? fvrDetails[i].fvrassignedDate : '') + '</td><td>' + (fvrDetails[i].fvrReceivedDate ? fvrDetails[i].fvrReceivedDate : '') + '</td><td>' +
                                        (fvrDetails[i].fvrTat ? fvrDetails[i].fvrTat : '') + '</td><td>' + (fvrDetails[i].status ? fvrDetails[i].status : '') + '</td><td>' + (fvrDetails[i].fvrGrading ? fvrDetails[i].fvrGrading : '') + '</td><td><a class="hreflink fvrGradingBtn" > View FVR Grading </a></td></td><td><a class="hreflink fvrViewDocBtn">View Document</a></td><tr>');
                                    $("#fvr_" + (i + 1)).attr('data-grading', JSON.stringify(fvrDetails[i]));
                                    $("#fvr_" + (i + 1)).attr('data-json', JSON.stringify(fvrDetails[i].dmsDocumentList));

                                }
                            }
                            $('#fvr-detail-modal').dialog('open');
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "history") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "history"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.claim_history_tbody').html('');
                            if (data.claimHistory) {
                                var history = data.claimHistory;
                                for (var i = 0; i < history.length; i++) {
                                    $('.claim_history_tbody').append('<tr><td>' + (history[i].typeofClaim ? history[i].typeofClaim : '') + '</td><td>' + (history[i].docrecdfrom ? history[i].docrecdfrom : '') + '</td><td>' + (history[i].rodtype ? history[i].rodtype : '') + '</td><td>' + (history[i].classification ? history[i].classification : '') + '</td><td>' + (history[i].referenceNo ? history[i].referenceNo : '') + '</td><td>' + (history[i].dateAndTime ? history[i].dateAndTime : '') + '</td><td>' + (history[i].userID ? history[i].userID : '') + '</td><td>' + (history[i].userName ? history[i].userName : '') + '</td><td>' + (history[i].claimStage ? history[i].claimStage : '') + '</td><td>' + (history[i].status ? history[i].status : '') + '</td><td>' + (history[i].userRemark ? history[i].userRemark : '') + '</td></tr>');
                                }
                            }
                            $('#claim_history_modal').dialog('open');
                        } else if (xhr.status == 204) {
                            $('#claim_history_modal').dialog('open');
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "preauthDetails") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "pre_auth_details",
                        preIntimationKey: 0
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            if (data.preAuthIntimationKeys) {
                                $('.preselect').html('');
                                $.each(data.preAuthIntimationKeys, function(key, value) {
                                    $('.preselect').append('<option value="' + key + '">' + value + '</option>');
                                });
                            }
                            if (data.preAuthDetails) {
                                getPreAuthDetails(data.preAuthDetails);
                            }
                            $('#preauth-details-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            alert("Preauth is not available");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "doctorNote") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "doctor_notes"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.doctor-trails-tbody').html('');
                            if (data.doctorNotes) {
                                var docNotes = data.doctorNotes;
                                for (var i = 0; i < docNotes.length; i++) {
                                    $('.doctor-trails-tbody').append('<tr><td>' + (docNotes[i].strNoteDate ? docNotes[i].strNoteDate : '') + '</td><td>' + (docNotes[i].userId ? docNotes[i].userId : '') + '</td><td>' + (docNotes[i].transaction ? docNotes[i].transaction : '') + '</td><td>' + (docNotes[i].transactionType ? docNotes[i].transactionType : '') + '</td><td>' + (docNotes[i].remarks ? docNotes[i].remarks : '') + '</td><tr>');
                                }
                            }
                            $('#doctor-trails-modal').dialog('open');
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "pedRequest") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "ped_request"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.ped-request-detail-tbody').html('');
                            if (data.pedRequestDetails) {
                                var pedDetails = data.pedRequestDetails;
                                for (var i = 0; i < pedDetails.length; i++) {
                                    $('.ped-request-detail-tbody').append('<tr id="ped_' + (i + 1) + '" data-json="" data-ammended=""  data-processed="" data-reviewed="" data-approved="" data-history=""><td>' + (i + 1) + '</td><td>' + (pedDetails[i].intimationNo ? pedDetails[i].intimationNo : '') + '</td><td>' + (pedDetails[i].pedSuggestionName ? pedDetails[i].pedSuggestionName : '') + '</td><td>' + (pedDetails[i].pedName ? pedDetails[i].pedName : '') + '</td><td>' + (pedDetails[i].repLetterDate ? pedDetails[i].repLetterDate : '') + '</td><td>' + (pedDetails[i].remarks ? pedDetails[i].remarks : '') + '</td><td>' + (pedDetails[i].requestorId ? pedDetails[i].requestorId : '') + '</td><td>' + (pedDetails[i].requestedDate ? pedDetails[i].requestedDate : '') + '</td><td>' + (pedDetails[i].requestStatus ? pedDetails[i].requestStatus : '') + '</td><td><a class="hreflink pedViewBtn" > View Details </a></td><td><a class="hreflink pedHistoryBtn">View Trails</a></td></tr>');
                                    $("#ped_" + (i + 1)).attr('data-json', JSON.stringify(pedDetails[i]));
                                    if (pedDetails[i].pedAmmended) {
                                        $("#ped_" + (i + 1)).attr('data-ammended', JSON.stringify(pedDetails[i].pedAmmended));
                                    }
                                    if (pedDetails[i].pedProcessed) {
                                        $("#ped_" + (i + 1)).attr('data-processed', JSON.stringify(pedDetails[i].pedProcessed));
                                    }
                                    if (pedDetails[i].pedReviewed) {
                                        $("#ped_" + (i + 1)).attr('data-reviewed', JSON.stringify(pedDetails[i].pedReviewed));
                                    }
                                    if (pedDetails[i].pedApproved) {
                                        $("#ped_" + (i + 1)).attr('data-approved', JSON.stringify(pedDetails[i].pedApproved));
                                    }
                                    if (pedDetails[i].pedHistoryList) {
                                        $("#ped_" + (i + 1)).attr('data-history', JSON.stringify(pedDetails[i].pedHistoryList));
                                    }
                                }
                            }
                            $('#PED-request-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            $('#PED-request-dialog').dialog('open');
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }

            }

            if ($('#selectbox').val() == "portability") {
                $('#dialog-modal-thirteen').dialog('open');
            }

            if ($('#selectbox').val() == "policyDocs") {
                var url = $('#getDmsViewUrl').val();
                if (url) {
                    window.open(url, "popUp", "resizable=yes");
                }
            }

            if ($('#selectbox').val() == "ackDetails") {
                $('.overlay').show();
                if (authToken)
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "acknowledgment_details"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            if (data) {
                                getAcknowledgementDetails(data);
                                $('#acknowledge-details-dialog').dialog('open');
                            }
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });

            }

            if ($('#selectbox').val() == "coorReply") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "coordinator_reply"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.coordinator-reply-body').html('');
                            if (data.coordinateDetails) {
                                var coorDetail = data.coordinateDetails;
                                for (var i = 0; i < coorDetail.length; i++) {
                                    $('.coordinator-reply-body').append('<tr id="coor_' + (i + 1) + '" data-docs="" data-name="coordinator"><td>' + (i + 1) + '</td><td>' + (coorDetail[i].requestedDate ? coorDetail[i].requestedDate : '') + '</td><td>' +
                                        (coorDetail[i].repliedDate ? coorDetail[i].repliedDate : '') + '</td><td>' + (coorDetail[i].requestType ? coorDetail[i].requestType : '') + '</td><td>' + (coorDetail[i].requestorRole ? coorDetail[i].requestorRole : '') + '</td><td>' + (coorDetail[i].requestroNameId ? coorDetail[i].requestroNameId : '') + '</td><td>' +
                                        (coorDetail[i].requestorRemarks ? coorDetail[i].requestorRemarks : '') + '</td><td><a class="viewFileBtn hreflink" > View File </a></td> <td>' +
                                        (coorDetail[i].coOrdinatorRepliedId ? coorDetail[i].coOrdinatorRepliedId : '') + '</td><td>' +
                                        (coorDetail[i].coOrdinatorRemarks ? coorDetail[i].coOrdinatorRemarks : '') + '</td></tr>');
                                    $("#coor_" + (i + 1)).attr('data-docs', JSON.stringify(coorDetail[i].uploadedDocumentList));
                                }
                            }
                            $('#coordinator-reply-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }

            }

            if ($('#selectbox').val() == "specilaistTrail") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "specialist_trail"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.specialist-trail-body').html('');
                            if (data.specialistOpinionDetails) {
                                var splDetail = data.specialistOpinionDetails;
                                for (var i = 0; i < splDetail.length; i++) {
                                    $('.specialist-trail-body').append('<tr id="spl_' + (i + 1) + '" data-docs="" data-name="specialist"><td>' + (i + 1) + '</td><td>' + (splDetail[i].requestedDate ? splDetail[i].requestedDate : '') + '</td><td>' +
                                        (splDetail[i].repliedDate ? splDetail[i].repliedDate : '') + '</td><td>' + (splDetail[i].specialistType ? splDetail[i].specialistType : '') + '</td><td>' + (splDetail[i].specialistDrNameId ? splDetail[i].specialistDrNameId : '') + '</td><td>' + (splDetail[i].requestorNameId ? splDetail[i].requestorNameId : '') + '</td><td>' +
                                        (splDetail[i].requestorRemarks ? splDetail[i].requestorRemarks : '') + '</td><td><a class="viewFileBtn hreflink" > View File </a></td> <td>' +
                                        (splDetail[i].specialistRemarks ? splDetail[i].specialistRemarks : '') + '</td></tr>');
                                    $("#spl_" + (i + 1)).attr('data-docs', JSON.stringify(splDetail[i].uploadedDocumentList));
                                }
                            }
                            $('#specialist-trail-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "claimDocs") {
                var url = $("#selectbox option:selected").attr("data-url");
                if (url) {
                    window.open(url, "popUp", "resizable=yes");
                }
            }


            if ($('#selectbox').val() == "64vbCompliance") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "view_64vb_compliance"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            if (data.filePath) {
                                var fileName = "64vbCompliance";
                                if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                                    $('canvas').remove();
                                    pdfData = base64ToUint8Array(data.filePath);
                                    loadPdf(pdfData);
                                    $('.toolSpan').html(data.fileName ? data.fileName : '');
                                    $('#download').attr('data-filename', data.fileName ? data.fileName : '');
                                    $('#64vb_complaince-dialog').dialog('open');
                                } else {
                                    $('#frame').attr("src", "data:application/pdf;base64, " + data.filePath);
                                    $('.frame-title').html('64 VB Compliance Report');
                                    document.getElementsByClassName('fvrDocumentViewModal')[0].style.display = "block";
                                }
                            }
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "sublimits") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "sublimits"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.sublimits-details-body').html('');
                            if (data.sublimitDetails) {
                                var subDetail = data.sublimitDetails;
                                for (var i = 0; i < subDetail.length; i++) {
                                    if (subDetail[i].subLimitAmt) {
                                        $('.sublimits-details-body').append('<tr><td>' + (i + 1) + '</td><td>' + (subDetail[i].section ? subDetail[i].section : '') + '</td><td>' +
                                            (subDetail[i].subLimitName ? subDetail[i].subLimitName : '') + '</td><td>' + (subDetail[i].subLimitAmt ? addCommas(subDetail[i].subLimitAmt) : '0') + '</td><td>' + (subDetail[i].includingCurrentClaimAmt ? addCommas(subDetail[i].includingCurrentClaimAmt) : '0') + '</td><td>' + (subDetail[i].includingCurrentClaimBal ? addCommas(subDetail[i].includingCurrentClaimBal) : '') + '</td><td>' +
                                            (subDetail[i].excludingCurrentClaimAmt ? addCommas(subDetail[i].excludingCurrentClaimAmt) : '0') + '</td> <td>' +
                                            (subDetail[i].excludingCurrentClaimBal ? addCommas(subDetail[i].excludingCurrentClaimBal) : '') + '</td><tr>');
                                    }
                                }
                                $('#sublimits-details-dialog').dialog('open');
                            }
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "hrmDetails") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "hrm_details"
                    }, function(data, statusText, xhr) {
                        $('.hrm-diagnosis-body').html('');
                        if (xhr.status == 200) {
                            if (data) {
                                $('#hrmIntimationno').html((data.intimationNo ? data.intimationNo : ''));
                                $('#hrmHospName').html((data.hospitalName ? data.hospitalName : ''));
                                $('#hrmId').html((data.hrmId ? data.hrmId : ''));
                                $('#hrmPhone').html((data.hrmPhone ? data.hrmPhone : ''));
                                $('#hrmName').html((data.hrmName ? data.hrmName : ''));
                                $('#hrmEmailId').html((data.hrmEmail ? data.hrmEmail : ''));

                                if (data.hrmDiagnosisList) {
                                    var hrmDetail = data.hrmDiagnosisList;
                                    for (var i = 0; i < hrmDetail.length; i++) {
                                        $('.hrm-diagnosis-body').append('<tr><td>' + (hrmDetail[i].anhOrNanh ? hrmDetail[i].anhOrNanh : '') + '</td><td>' + (hrmDetail[i].diagnosis ? hrmDetail[i].diagnosis : '') + '</td><td>' + (hrmDetail[i].surgicalProcedure ? hrmDetail[i].surgicalProcedure : '') + '</td><td>' + (hrmDetail[i].claimedAmt ? hrmDetail[i].claimedAmt : '') + '</td><td>' +
                                            (hrmDetail[i].packageAmt ? hrmDetail[i].packageAmt : '') + '</td> <td>' + (hrmDetail[i].requestTypeValue ? hrmDetail[i].requestTypeValue : '') + '</td><td>' +
                                            (hrmDetail[i].docRemarks ? hrmDetail[i].docRemarks : '') + '</td> <td>' +
                                            (hrmDetail[i].assigneeDateAndTimeStr ? hrmDetail[i].assigneeDateAndTimeStr : '') + '</td><td>' + (hrmDetail[i].hrmReplyRemarks ? hrmDetail[i].hrmReplyRemarks : '') + '</td><td>' + (hrmDetail[i].replyDateAndTime ? hrmDetail[i].replyDateAndTime : '') + '</td> <td>' + (hrmDetail[i].docUserId ? hrmDetail[i].docUserId : '') + '</td><td>' + (hrmDetail[i].docName ? hrmDetail[i].docName : '') + '</td> <td>' + (hrmDetail[i].docDeskNumber ? hrmDetail[i].docDeskNumber : '') + '</td></tr>');
                                    }
                                }
                            }
                            $('#hrm-details-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "riskDetails") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "risk_details"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            if (data) {
                                $('#riskPolicyNo').html((data.policyNo != null ? data.policyNo : ''));
                                $('#riskName').html((data.policyName != null ? data.policyName : ''));
                                $('#riskPolicyFrom').html((data.policyFromDate != null ? data.policyFromDate : ''));
                                $('#riskSumInsured').html((data.sumInsured != null ? data.sumInsured : ''));
                                $('#riskPolicyTo').html((data.policyToDate != null ? data.policyToDate : ''));
                                $('#riskRelationship').html((data.relationship != null ? data.relationship : ''));
                                $('#riskAge').html((data.age != null ? data.age : ''));
                                $('#riskPED').html((data.riskPED != null ? data.riskPED : ''));
                                $('#riskPortalPED').html((data.portalPED != null ? data.portalPED : ''));
                            }
                            $("table.riskDetailsTable tr:even").css("background-color", "#c5e0dc");
                            $('#risk-details-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }


            if ($('#selectbox').val() == "previousInsurance") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "previous_insurance_details"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.previous-insurance-table-body').html('');
                            if (data.prevInsuranceDetails) {
                                var insuranceDetail = data.prevInsuranceDetails;
                                for (var i = 0; i < insuranceDetail.length; i++) {
                                    $('.previous-insurance-table-body').append('<tr id="prevIns_' + (i + 1) + '" data-json=""><td>' + (insuranceDetail[i].previousInsurerName ? insuranceDetail[i].previousInsurerName : '') + '</td><td>' + (insuranceDetail[i].policyNumber ? insuranceDetail[i].policyNumber : '') + '</td><td>' + (insuranceDetail[i].policyFromDate ? insuranceDetail[i].policyFromDate : '') + '</td><td>' + (insuranceDetail[i].policyToDate ? insuranceDetail[i].policyToDate : '') + '</td><td>' + (insuranceDetail[i].underWritingYear ? insuranceDetail[i].underWritingYear : '') + '</td> <td>' + (insuranceDetail[i].sumInsured ? insuranceDetail[i].sumInsured : '') + '</td><td>' + (insuranceDetail[i].productName ? insuranceDetail[i].productName : '') + '</td><td><a class="prevPolicyView hreflink" data-url="' + (insuranceDetail[i].policyScheduleUrl ? insuranceDetail[i].policyScheduleUrl : '') + '"> View Policy Schedule </a></td></td><td><a class="prevInsDetails hreflink"> View Insured Details </a></td></tr>');
                                    $("#prevIns_" + (i + 1)).attr('data-json', JSON.stringify(insuranceDetail[i].previousInsuredDetails));

                                }
                            }
                            $('#previous-insurance-details-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            $('#previous-insurance-details-dialog').dialog('open');
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }
            if ($('#selectbox').val() == "sumInsured") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "balance_sum_insured"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            if (data.isPASumInsured == false) {
                                $('#blPolicyNo').html((data.policyNo ? data.policyNo : ''));
                                $('#blIntNo').html((data.intimatioNo ? data.intimatioNo : ''));
                                $('#blInsName').html((data.insuredName ? data.insuredName : ''));
                                $('#blProductCode').html((data.productCode ? data.productCode : ''));
                                $('#blProductName').html((data.productName ? data.productName : ''));
                                $('#blOriginalSI').html((data.originalSI ? data.originalSI : ''));
                                $('#blRestoredSI').html((data.restoredSI ? data.restoredSI : ''));
                                $('#blCumBonus').html((data.cummulativeBonus ? data.cummulativeBonus : ''));
                                $('#blRechargedSI').html((data.rechargedSI ? data.rechargedSI : ''));
                                $('#blLimit').html((data.limitcoverage ? data.limitcoverage : ''));

                                if (data.isGmc == true) {
                                    $('.blSecCode').removeClass('hidden');
                                    $('#blSecCode').html((data.gmcPolicySecCode ? data.gmcPolicySecCode : ''));
                                    if (data.restoredSumInsured >= 1) {
                                        $('.provisionClaim').removeClass('hidden');
                                        $('.siOutStand').removeClass('hidden');
                                        $('.claimsPaid').removeClass('hidden');
                                        $('#provisionClaim').html((data.currentClaimProvision ? data.currentClaimProvision : ''));
                                        $('#siOutStand').html((data.claimsOutStanding ? data.claimsOutStanding : ''));
                                        $('#claimsPaid').html((data.claimsPaid ? data.claimsPaid : ''));
                                    }
                                } else {
                                    $('.blSecCode').addClass('hidden');
                                    $('.provisionClaim').addClass('hidden');
                                    $('.siOutStand').addClass('hidden');
                                    $('.claimsPaid').addClass('hidden');
                                    $('#blSecCode').html('');
                                    $('#siOutStand').html('');
                                    $('#provisionClaim').html('');
                                    $('#claimsPaid').html('');
                                    if (data.isSuperSurplus == true) {
                                        $('.definedLimit').removeClass('hidden');
                                        $('#definedLimit').html((data.definedLimit ? data.definedLimit : ''));
                                    } else {
                                        $('.definedLimit').addClass('hidden');
                                        $('#definedLimit').html('');
                                    }
                                }

                                if (data.isGmcSections == true) {
                                    $('.gmcMainMember').removeClass('hidden');
                                    $('.gmcInnerLimitAppl').removeClass('hidden');
                                    $('.gmcInnerLimitInsured').removeClass('hidden');
                                    $('.gmcInnerLimitUtilised').removeClass('hidden');
                                    $('.gmcInnerLimitAval').removeClass('hidden');
                                    $('#gmcMainMember').html((data.gmcMainMember ? data.gmcMainMember : ''));
                                    $('#gmcInnerLimitAppl').html((data.gmcInnerLimitAppl ? data.gmcInnerLimitAppl : ''));
                                    $('#gmcInnerLimitInsured').html((data.gmcInnerLimitInsured ? data.gmcInnerLimitInsured : ''));
                                    $('#gmcInnerLimitUtilised').html((data.gmcInnerLimitUtilised ? data.gmcInnerLimitUtilised : ''));
                                    $('#gmcInnerLimitAval').html((data.gmcInnerLimitAval ? data.gmcInnerLimitAval : ''));
                                } else {
                                    $('.gmcMainMember').addClass('hidden');
                                    $('.gmcInnerLimitAppl').addClass('hidden');
                                    $('.gmcInnerLimitInsured').addClass('hidden');
                                    $('.gmcInnerLimitUtilised').addClass('hidden');
                                    $('.gmcInnerLimitAval').addClass('hidden');
                                    $('#gmcMainMember').html('');
                                    $('#gmcInnerLimitAppl').html('');
                                    $('#gmcInnerLimitInsured').html('');
                                    $('#gmcInnerLimitUtilised').html('');
                                    $('#gmcInnerLimitAval').html('');
                                }

                                $("table.sumInsuredTable tr:even").css("background-color", "#c5e0dc");

                                $('.sum_insured_tables').html('');
                                if (data.hasHospTable == true) {
                                    $('.sum_insured_tables').append(sumInsuredTableFormat(data.hospTableDetails, 'sectionI'));
                                }
                                if (data.hasNewBornTable == true) {
                                    $('.sum_insured_tables').append(sumInsuredTableFormat(data.newBornTableDetails, 'sectionII'));
                                }
                                if (data.hasOutPatientTable == true) {
                                    $('.sum_insured_tables').append(sumInsuredTableFormat(data.outPatientTableDetails, 'sectionIII'));
                                }
                                if (data.hasHospCashTable == true) {
                                    $('.sum_insured_tables').append(sumInsuredTableFormat(data.hospCashTableDetails, 'sectionIV'));
                                }
                                if (data.hasHealthCheckTable == true) {
                                    $('.sum_insured_tables').append(sumInsuredTableFormat(data.healthCheckTableDetails, 'sectionV'));
                                }
                                if (data.hasBariatricTable == true) {
                                    $('.sum_insured_tables').append(sumInsuredTableFormat(data.bariatricTableDetails, 'sectionVI'));
                                }
                                if (data.hasLumpSumTable == true) {
                                    $('.sum_insured_tables').append(sumInsuredTableFormat(data.lumpSumTableDetails, 'sectionVIII'));
                                }

                                $('.optional_sum_insured_tbody').html('');
                                if (data.hasOptionalTable) {
                                    var optionalData = data.hasOptionalTable;
                                    $('.optional_sum_insured_tbody').append('<tr><td>' + (json[i].subCover ? json[i].subCover : '') + '</td><td>' + (json[i].limit ? addCommas(json[i].limit) : '') + '</td><td>' + (json[i].claimPaid ? addCommas(json[i].claimPaid) : '') + '</td><td>' + (json[i].claimOutstanding ? addCommas(json[i].claimOutstanding) : '') + '</td><td>' + (json[i].balance ? addCommas(json[i].balance) : '') + '</td><td>' + (json[i].provisionCurrentClaim ? addCommas(json[i].provisionCurrentClaim) : '') + '</td><td>' + (json[i].balanceSI ? addCommas(json[i].balanceSI) : '') + '</td></tr>');
                                }
                                $('.PA_sum_insured_tables').addClass('hidden');
                                $('.PA_sum_insured').addClass('hidden');
                                $('.sum_insured_tables').removeClass('hidden');
                                $('.sum_insured').removeClass('hidden');
                                $('.optional_sum_insured').removeClass('hidden');
                            } else {
                                $("table.sumInsuredTable").css("background-color", "#c5e0dc");
                                $('#blPAInsName').html((data.insuredName ? data.insuredName : ''));
                                $('#blPAInsAge').html((data.insuredAge ? data.insuredAge : ''));
                                $('.PA_sum_insured_tables').removeClass('hidden');
                                $('.PA_sum_insured').removeClass('hidden');
                                $('.sum_insured_tables').addClass('hidden');
                                $('.sum_insured').addClass('hidden');
                                $('.balanceSumInsured').html('');
                                $('.optional_sum_insured').addClass('hidden');
                                if (data.balanceSumInsured) {
                                    var sumIns = data.balanceSumInsured;
                                    for (var i = 0; i < sumIns.length; i++) {
                                        $('.balanceSumInsured').append('<tr><td>' + (sumIns[i].orginalSI ? sumIns[i].orginalSI : '') + '</td><td>' + (sumIns[i].cumulativeBonus ? sumIns[i].cumulativeBonus : '0') + '</td><td>' + (sumIns[i].totalSI ? sumIns[i].totalSI : '0') + '</td><td>' + (sumIns[i].claimPaid ? sumIns[i].claimPaid : '0') + '</td><td>' + (sumIns[i].claimOutStanding ? sumIns[i].claimOutStanding : '0') + '</td> <td>' + (sumIns[i].balanceSI ? sumIns[i].balanceSI : '0') + '</td><td>' + (sumIns[i].currentClaim ? sumIns[i].currentClaim : '0') + '</td><td>' + (sumIns[i].availableSI ? sumIns[i].availableSI : '0') + '</td></tr>');
                                    }
                                }

                                $('.balanceSumInsuredBenefits').html('');
                                if (data.balanceSumInsuredBenefits) {
                                    var sumIns = data.balanceSumInsuredBenefits;
                                    for (var i = 0; i < sumIns.length; i++) {
                                        $('.balanceSumInsuredBenefits').append('<tr><td>' + (sumIns[i].coverDesc ? sumIns[i].coverDesc : '') + '</td><td>' + (sumIns[i].orginalSI ? sumIns[i].orginalSI : '0') + '</td><td>' + (sumIns[i].cumulativeBonus ? sumIns[i].cumulativeBonus : '0') + '</td><td>' + (sumIns[i].totalSI ? sumIns[i].totalSI : '0') + '</td><td>' + (sumIns[i].claimPaid ? sumIns[i].claimPaid : '0') + '</td><td>' + (sumIns[i].claimOutStanding ? sumIns[i].claimOutStanding : '0') + '</td><td>' + (sumIns[i].currentClaim ? sumIns[i].currentClaim : '0') + '</td><td>' + (sumIns[i].availableSI ? sumIns[i].availableSI : '0') + '</td></tr>');
                                    }
                                }

                                $('.gpaSumInsuredCovers').html('');
                                if (data.gpaSumInsuredCovers) {
                                    var sumIns = data.gpaSumInsuredCovers;
                                    for (var i = 0; i < sumIns.length; i++) {
                                        $('.gpaSumInsuredCovers').append('<tr><td>' + (sumIns[i].coverDesc ? sumIns[i].coverDesc : '') + '</td><td>' + (sumIns[i].totalSI ? sumIns[i].totalSI : '0') + '</td><td>' + (sumIns[i].claimPaid ? sumIns[i].claimPaid : '0') + '</td><td>' + (sumIns[i].claimOutStanding ? sumIns[i].claimOutStanding : '0') + '</td><td>' + (sumIns[i].currentClaim ? sumIns[i].currentClaim : '0') + '</td><td>' + (sumIns[i].availableSI ? sumIns[i].availableSI : '0') + '</td></tr>');
                                    }
                                }

                                $('.balanceSumInsuredAddOn').html('');
                                if (data.balanceSumInsuredAddOn) {
                                    var sumIns = data.balanceSumInsuredAddOn;
                                    for (var i = 0; i < sumIns.length; i++) {
                                        $('.balanceSumInsuredAddOn').append('<tr><td>' + (sumIns[i].coverDesc ? sumIns[i].coverDesc : '') + '</td><td>' + (sumIns[i].totalSI ? sumIns[i].totalSI : '0') + '</td><td>' + (sumIns[i].claimPaid ? sumIns[i].claimPaid : '0') + '</td><td>' + (sumIns[i].claimOutStanding ? sumIns[i].claimOutStanding : '0') + '</td><td>' + (sumIns[i].currentClaim ? sumIns[i].currentClaim : '0') + '</td><td>' + (sumIns[i].availableSI ? sumIns[i].availableSI : '0') + '</td></tr>');
                                    }
                                }


                            }
                            $('#balance-sum-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "topPanel") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "top_panel_details"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            if (data.topPanelDetails) {
                                data = data.topPanelDetails;
                                $('#topIntNo').html((data.intimationNo ? data.intimationNo : ''));
                                $('#topAppAmt').html((data.approvalAmt ? data.approvalAmt : ''));
                                $('#topClaimNo').html((data.claimNo ? data.claimNo : ''));
                                $('#topBalSI').html((data.balanceSI ? data.balanceSI : ''));
                                $('#topPatientName').html((data.patientName ? data.patientName : ''));
                                $('#topPolicyNo').html((data.policyNo ? data.policyNo : ''));
                                $('#topInsName').html((data.insuredName ? data.insuredName : ''));
                                $('#topProductName').html((data.productName ? data.productName : ''));
                                $('#topInsAge').html((data.insuredAge ? data.insuredAge : ''));
                                $('#topPolicyYear').html((data.policyYear ? data.policyYear : ''));
                                $('#topClaimType').html((data.claimType ? data.claimType : ''));
                                $('#topPolicyPeriod').html((data.policyPeriod ? data.policyPeriod : ''));
                                $('#topAdmissionDate').html((data.admissionDate ? data.admissionDate : ''));
                                $('#topPolicyAge').html((data.policyAge ? data.policyAge : ''));
                                $('#topDischargeDate').html((data.dischargeDate ? data.dischargeDate : ''));
                                $('#topPolicyType').html((data.policyType ? data.policyType : ''));
                                $('#topDiagnosis').html((data.diagnosis ? data.diagnosis : ''));
                                $('#topInceptionDate').html((data.inceptionDate ? data.inceptionDate : ''));
                                $('#topHosType').html((data.hospType ? data.hospType : ''));
                                $('#topProducttype').html((data.producttype ? data.producttype : ''));
                                $('#topNetHospType').html((data.netHospType ? data.netHospType : ''));
                                $('#topPlan').html((data.plan ? data.plan : '-'));
                                $('#topHospCode').html((data.hospCode ? data.hospCode : ''));
                                $('#topBusinessClass').html((data.businessClass ? data.businessClass : ''));
                                $('#topHospName').html((data.hospName ? data.hospName : ''));
                                $('#topCustId').html((data.custId ? data.custId : ''));
                                $('#topHospCity').html((data.hospCity ? data.hospCity : ''));
                                $('#topCpuCode').html((data.cpuCode ? data.cpuCode : ''));
                                $('#topHospIrdaCode').html((data.hospIrdaCode ? data.hospIrdaCode : ''));
                                $('#topPortability').html((data.portability ? data.portability : ''));
                                $('#topOriginalSI').html((data.originalSI ? data.originalSI : '0.0'));
                                $('#topSrClaim').html((data.seniorClaim ? data.seniorClaim : ''));
                            }
                            $('#top-panel-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            $('#top-panel-dialog').dialog('open');
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "irdaNonPayables") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "irda_non_payables"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            if (data.irdaUrl) {
                                $('#frameContent').attr("src", data.irdaUrl);
                            }
                            $('#irda-non-payables-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "irdaBillDetails") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "irda_category_details"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.billdetails-details-body').html('');
                            if (data.irdaBillDetails) {
                                var billDetail = data.irdaBillDetails;
                                for (var i = 0; i < billDetail.length; i++) {
                                    $('.billdetails-details-body').append('<tr><td>' + (i + 1) + '</td><td>' + (billDetail[i].irdaCode ? billDetail[i].irdaCode : '') + '</td><td>' + (billDetail[i].category ? billDetail[i].category : '') + '</td><td>' + (billDetail[i].noOfDays ? billDetail[i].noOfDays : '') + '</td><td>' + (billDetail[i].perDayAmt ? billDetail[i].perDayAmt : '') + '</td><td>' + (billDetail[i].claimedAmount ? billDetail[i].claimedAmount : '') + '</td><td>' + (billDetail[i].billingNonPayableAmt ? billDetail[i].billingNonPayableAmt : '') + '</td><td>' + (billDetail[i].netAmount ? billDetail[i].netAmount : '') + '</td><td>' + (billDetail[i].entitlementNoOfDays ? billDetail[i].entitlementNoOfDays : '') + '</td><td>' + (billDetail[i].entitlementPerDayAmt ? billDetail[i].entitlementPerDayAmt : '') + '</td><td>' + (billDetail[i].amount ? billDetail[i].amount : '') + '</td><td>' + (billDetail[i].deductionNonPayableAmount ? billDetail[i].deductionNonPayableAmount : '') + '</td><td>' + (billDetail[i].payableAmount ? billDetail[i].payableAmount : '') + '</td></tr>');
                                }
                                $("table.irdaBillDetailsTable tr:even").css("background-color", "#c2cbce !important");
                                $('.billdetails-details-body').append('<tr class="subtotal"><td></td><td></td><td></td><td></td><td></td><td>' + data.claimedAmount + '</td><td></td><td>' + data.netAmount + '</td><td></td><td></td><td>' + data.amount + '</td><td>' + data.nonPayableAmount + '</td><td>' + data.payableAmount + '</td></tr>');

                            }
                            $('#irda-bill-details-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                            $('#irda-bill-details-dialog').dialog('open');
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "productBenefits") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "product_benefits"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.product_condition_tbody').html('');
                            $('.daycare_benefits_tbody').html('');

                            if (data) {
                                if (data.isGmcProduct) {
                                    if (data.gmcBenefits) {
                                        var benefits = data.gmcBenefits;
                                        for (var i = 0; i < benefits.length; i++) {
                                            $('.gmc_product_benefits_tbody').append('<tr><td>' + (benefits[i].conditionCode ? benefits[i].conditionCode : '') + '</td><td>' + (benefits[i].description ? benefits[i].description : '') + '</td><td>' + (benefits[i].longDescription ? benefits[i].longDescription : '') + '</td></tr>');
                                        }
                                    }
                                    $('.productBenefits').addClass('hidden');
                                    $('.gmc_product_benefits').removeClass('hidden');
                                } else {
                                    $('#productBenefitsName').val((data.productName ? data.productName : ''));
                                    $('#productBenefitsCode').val((data.productCode ? data.productCode : ''));
                                    if (data.policyConditions) {
                                        var condition = data.policyConditions;
                                        for (var i = 0; i < condition.length; i++) {
                                            $('.product_condition_tbody').append('<tr><td>' + (condition[i].description ? condition[i].description : '') + '</td><td>' + (condition[i].productRules ? condition[i].productRules : '') + '</td></tr>');
                                        }
                                    }
                                    if (data.benefits) {
                                        var benefits = data.benefits;
                                        for (var i = 0; i < benefits.length; i++) {
                                            $('.daycare_benefits_tbody').append('<tr><td>' + (benefits[i].dayCareBenefits ? benefits[i].dayCareBenefits : '') + '</td><td>' + (benefits[i].dayCareLimits ? benefits[i].dayCareLimits : '') + '</td></tr>');
                                        }
                                    }
                                    $('.gmc_product_benefits').addClass('hidden');
                                    $('.productBenefits').removeClass('hidden');
                                }
                            }
                            $('#product-benefits-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            $('#product-benefits-dialog').dialog('open');
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }

            }

            if ($('#selectbox').val() == "currentPolicy") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "current_policy_details"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            if (data) {
                                $("#p_policyNo").html((data.policyNo ? data.policyNo : ''));
                                $("#p_prevPolicy").html((data.prevPolicyNo ? data.prevPolicyNo : ''));
                                $("#p_endorseNo").html((data.endorsementNo ? data.endorsementNo : ''));
                                $("#p_proposerCode").html((data.proposerCode ? data.proposerCode : ''));
                                $("#p_officeCode").html((data.issueOfficeCode ? data.issueOfficeCode : ''));
                                $("#p_proposerName").html((data.proposerName ? data.proposerName : ''));
                                $("#p_address").html((data.address ? data.address : ''));
                                $("#p_prev_address").html((data.prevAddress ? data.prevAddress : ''));
                                $("#p_contact").html((data.contactDetails ? data.contactDetails : ''));
                                $("#p_prev_contact").html((data.prevContactDetails ? data.prevContactDetails : ''));
                                $("#p_recpt_no").html((data.recptNo ? data.recptNo : ''));
                                $("#p_recpt_date").html((data.recptDate ? data.recptDate : ''));
                                $("#p_officer").html((data.executive ? data.executive : ''));
                                $("#p_agent").html((data.agent ? data.agent : ''));
                                $("#p_insPeriod").html((data.insurancePeriod ? data.insurancePeriod : ''));
                                $("#p_productName").html((data.productName ? data.productName : ''));
                                $("#p_premium").html((data.grossPremium ? addCommas(data.grossPremium) : ''));
                                $("#p_stampDuty").html((data.stampDuty ? data.stampDuty : ''));
                                $("#p_tax").html((data.serviceTax ? data.serviceTax : ''));
                                $("#p_total").html((data.totalPremium ? addCommas(data.totalPremium) : ''));
                                $('.policy_details_insured_tbody').html('');
                                if (data.isInsuredGmc == true && data.insuredDetails) {
                                    var insured = data.insuredDetails;
                                    for (var i = 0; i < insured.length; i++) {
                                        $('.policy_details_insured_tbody').append('<tr><td>' + (insured[i].insuredName ? insured[i].insuredName : '') + '</td><td>' + (insured[i].sex ? insured[i].sex : '') + '</td><td>' + (insured[i].dateOfBirth ? insured[i].dateOfBirth : '') + '</td><td>' + (insured[i].age ? insured[i].age : '') + '</td><td>' + (insured[i].relation ? insured[i].relation : '') + '</td><td>' + (insured[i].sumInsured ? addCommas(insured[i].sumInsured) : '') + '</td><td>' + (insured[i].pedDescription ? insured[i].pedDescription : 'nil') + '</td></tr>');
                                    }
                                    $("table.policy_details_insured_table tr:even").css("background-color", "#c5e0dc");
                                    $('.policy_details_insured_table').removeClass('hidden');

                                } else {
                                    $('.policy_details_insured_table').addClass('hidden');
                                }

                                if (data.documentUrl) {
                                    $('#currentPolicySchedule').attr('data-url', data.documentUrl);
                                }
                            }

                            $('#current_policy-details-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            $('#current_policy-details-dialog').dialog('open');
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "policyDetails") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "policy_details"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            var url = data.policyDetailUrl;
                            if (url) {
                                window.open(url, "popUp", "resizable=yes");
                            }
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "billSummary") {
                $('.overlay').show();
                getBillSummaryDetails();
                $('.overlay').hide();
            }

            if ($('#selectbox').val() == "translationMisc") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "translation_misc"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.translation-misc-tbody').html('');
                            if (data.translationMiscDetails) {
                                var miscDetails = data.translationMiscDetails;
                                for (var i = 0; i < miscDetails.length; i++) {
                                    $('.translation-misc-tbody').append('<tr id="misc_' + (i + 1) + '" data-docs=""><td>' + (i + 1) + '</td><td>' + (miscDetails[i].requestedDate ? miscDetails[i].requestedDate : '') + '</td><td>' + (miscDetails[i].repliedDate ? miscDetails[i].repliedDate : '') + '</td><td>' + (miscDetails[i].requestType ? miscDetails[i].requestType : '') + '</td><td>' + (miscDetails[i].requestorRole ? miscDetails[i].requestorRole : '') + '</td><td>' + (miscDetails[i].requestroNameId ? miscDetails[i].requestroNameId : '') + '</td><td>' + (miscDetails[i].requestorRemarks ? miscDetails[i].requestorRemarks : '') + '</td><td>' + (miscDetails[i].coOrdinatorRepliedId ? miscDetails[i].coOrdinatorRepliedId : '') + '</td><td>' + (miscDetails[i].coOrdinatorRemarks ? miscDetails[i].coOrdinatorRemarks : '') + '</td><td>' + (miscDetails[i].uploadedDocumentList ? '<a class="viewFileBtn hreflink" > View File </a>' : '<a class="fileNotFoundBtn hreflink" data-message="Details not found"> View File </a>') + '</td></tr>');
                                    $("#misc_" + (i + 1)).attr('data-docs', JSON.stringify(miscDetails[i].uploadedDocumentList));
                                }

                                $("table.translation-misc-tbody tr:even").css("background-color", "#c2cbce !important");
                                $('#translation-misc-modal').dialog('open');
                            }
                        } else if (xhr.status == 204) {
                            $('#translation-misc-modal').dialog('open');
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "previousClaim") {
                getProcessClaimDetails();
                return false;
            }

            if ($('#selectbox').val() == "earlierAckDetails") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "earlier_acknowlegment_details"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.earlier-ack-detail-tbody').html('');
                            if (data.earlierAckDetails) {
                                var earlierAckDetails = data.earlierAckDetails;
                                for (var i = 0; i < earlierAckDetails.length; i++) {
                                    $('.earlier-ack-detail-tbody').append('<tr data-intimationNo="' + (earlierAckDetails[i].intimationNumber ? earlierAckDetails[i].intimationNumber : '') + '"><td>' + (i + 1) + '</td><td>' +
                                        (earlierAckDetails[i].acknowledgeNumber ? earlierAckDetails[i].acknowledgeNumber : '') + '</td><td>' + (earlierAckDetails[i].rodNumber ? earlierAckDetails[i].rodNumber : '') + '</td><td>' + (earlierAckDetails[i].receivedFromValue ? earlierAckDetails[i].receivedFromValue : '') + '</td><td>' +
                                        (earlierAckDetails[i].strDocumentReceivedDate ? earlierAckDetails[i].strDocumentReceivedDate : '') + '</td><td>' + (earlierAckDetails[i].modeOfReceipt ? earlierAckDetails[i].modeOfReceipt.value : '') + '</td><td>' + (earlierAckDetails[i].billClassification ? earlierAckDetails[i].billClassification : '') + '</td><td>' + (earlierAckDetails[i].approvedAmount ? earlierAckDetails[i].approvedAmount : '') + '</td><td>' +
                                        (earlierAckDetails[i].corpBufferUtilised ? earlierAckDetails[i].corpBufferUtilised : '') + '</td><td>' + (earlierAckDetails[i].claim ? (earlierAckDetails[i].claim.status ? earlierAckDetails[i].claim.status.userStatus : '') : '') + '</td><td><a class="hreflink prevClaimStatusClk" > View Claim Status </a></td></td><td><a data-url="' + (earlierAckDetails[i].subclassification ? earlierAckDetails[i].subclassification : '') + '" class="hreflink earlierDocButton">View Documents</a></td><tr>');

                                }
                            }
                            $('#earlier-ack-detail-modal').dialog('open');
                        } else if (xhr.status == 204) {
                            alert("Details not found");
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }
            }

            if ($('#selectbox').val() == "investigationDetails") {
                $('.overlay').show();
                if (authToken) {
                    $.get('crcportal', {
                        token: authToken,
                        pageName: "investigation_details"
                    }, function(data, statusText, xhr) {
                        if (xhr.status == 200) {
                            $('.investigation-request-detail-tbody').html('');
                            if (data.investigationDetails) {
                                var investDetails = data.investigationDetails;
                                for (var i = 0; i < investDetails.length; i++) {
                                    $('.investigation-request-detail-tbody').append('<tr id="inv_' + (i + 1) + '" data-json="" ><td>' + (i + 1) + '</td><td>' + (investDetails[i].investigatorName ? investDetails[i].investigatorName : '') + '</td><td>' + (investDetails[i].investigatorCode ? investDetails[i].investigatorCode : '') + '</td><td>' + (investDetails[i].investigatorContactNo ? investDetails[i].investigatorContactNo : '') + '</td><td>' + (investDetails[i].hospitalName ? investDetails[i].hospitalName : '') + '</td><td>' + (investDetails[i].investigationTriggerPoints ? investDetails[i].investigationTriggerPoints : '') + '</td><td>' + (investDetails[i].investigationAssignedDate ? investDetails[i].investigationAssignedDate : '') + '</td><td>' + (investDetails[i].investigationCompletedDateStr ? investDetails[i].investigationCompletedDateStr : '') + '</td><td>' + (investDetails[i].investigationReportReceivedDate ? investDetails[i].investigationReportReceivedDate : '') + '</td><td>' + (investDetails[i].tat ? investDetails[i].tat : '') + '</td><td>' + (investDetails[i].status ? investDetails[i].status : '') + '</td><td><a data-key="' + (investDetails[i].investigationKey ? investDetails[i].investigationKey : '') + '" data-assigned-key="' + (investDetails[i].investigAssignedKey ? investDetails[i].investigAssignedKey : '') + '" class="hreflink investigatorViewBtn"> View Details </a></td><td><a class="hreflink investigationHistoryBtn">Investigation Assigned History</a></td></tr>');
                                    $("#inv_" + (i + 1)).attr('data-json', JSON.stringify(investDetails[i].assignedInvestigationHistoryDTO));

                                }
                            }
                            $('#investigation-request-dialog').dialog('open');
                        } else if (xhr.status == 204) {
                            $('#investigation-request-dialog').dialog('open');
                        } else if (xhr.status == 400) {
                            alert("Bad Request");
                        }
                        $('.overlay').hide();
                    });
                }

            }


        });

        $(".hospitalpackage").click(function() {
            $("#hospitaldialog").attr('src', $(this).attr("href"));
            $("#hospitaldiv").dialog({
                width: 1000,
                height: 1000,
                modal: true,
                close: function() {
                    $("#hospitaldialog").attr('src', "about:blank");
                }
            });
            return false;
        });

        jQuery('.tabs .tab-links a').on('click', function(e) {
            var currentAttrValue = jQuery(this).attr('href');
            // Show/Hide Tabs
            jQuery('.tabs ' + currentAttrValue).show().siblings().hide();
            // Change/remove current tab to active
            jQuery(this).parent('li').addClass('active').siblings().removeClass('active');
            e.preventDefault();
        });


    });

    /**
     * JS code to display base64-encoded string as pdf in Internet Explorer
     */
    function base64ToUint8Array(base64) {
        var raw = atob(base64); //This is a native function that decodes a base64-encoded string.
        var uint8Array = new Uint8Array(new ArrayBuffer(raw.length));
        for (var i = 0; i < raw.length; i++) {
            uint8Array[i] = raw.charCodeAt(i);
        }

        return uint8Array;
    }

    function loadPdf(pdfData) {
        PDFJS.disableWorker = true;
        var pdf = PDFJS.getDocument(pdfData);
        pdf.then(renderPdf);
    }

    function renderPdf(pdf) {
        pdf.getPage(1).then(renderPage);
    }

    var canvas;

    function renderPage(page) {
        var viewport = page.getViewport(scale);
        var $canvas = jQuery("<canvas></canvas>");

        //Set the canvas height and width to the height and width of the viewport
        canvas = $canvas.get(0);
        var context = canvas.getContext("2d");
        canvas.height = viewport.height;
        canvas.width = viewport.width;

        //Append the canvas to the pdf container div
        jQuery(".64VBFrame").append($canvas);

        //The following few lines of code set up scaling on the context if we are on a HiDPI display
        var outputScale = getOutputScale();
        if (outputScale.scaled) {
            var cssScale = 'scale(' + (1 / outputScale.sx) + ', ' + (1 / outputScale.sy) + ')';
            CustomStyle.setProp('transform', canvas, cssScale);
            CustomStyle.setProp('transformOrigin', canvas, '0% 0%');

            if ($textLayerDiv.get(0)) {
                CustomStyle.setProp('transform', $textLayerDiv.get(0), cssScale);
                CustomStyle.setProp('transformOrigin', $textLayerDiv.get(0), '0% 0%');
            }
        }

        context._scaleX = outputScale.sx;
        context._scaleY = outputScale.sy;
        if (outputScale.scaled) {
            context.scale(outputScale.sx, outputScale.sy);
        }
        var canvasOffset = $canvas.offset();

        page.getTextContent().then(function(textContent) {
            var renderContext = {
                canvasContext: context,
                viewport: viewport
            };

            page.render(renderContext);
        });
    }

    // View Portability Details
    $(".portDetailsBtn").click(function() {
        var portDetails = JSON.parse($(this).attr('data-json'));
        $('.portDetailsBody').html('');
        $('.portDetailsBody').append(portabilityTableFormat(portDetails));
        document.getElementsByClassName('portDetailsModal')[0].style.display = "block";
    });

    function ordinal_suffix_of(i) {
        var j = i % 10;
        var k = i % 100;
        if (j == 1 && k != 11) {
            return i + "st";
        }
        if (j == 2 && k != 12) {
            return i + "nd";
        }
        if (j == 3 && k != 13) {
            return i + "rd";
        }
        return i + "th";
    }

    function addCommas(nStr) {
        nStr += '';
        var x = nStr.split('.');
        var x1 = x[0];
        var x2 = x.length > 1 ? '.' + x[1] : '';
        var rgx = /(\d+)(\d{3})/;
        while (rgx.test(x1)) {
            x1 = x1.replace(rgx, '$1' + ',' + '$2');
        }
        return x1 + x2;
    }

    $('.policyYear').on('change', function() {
        getProcessClaimDetails();
    });

    $("input[name=claimType]").click(function() {
        getProcessClaimDetails();
    });

    function getProcessClaimDetails() {
        $('.overlay').show();
        $('.inner-overlay').show();
        var claimType = $('input[name=claimType]:checked').val();
        var claimLabel = $('input[name=claimType]:checked').attr('data-label');
        var policyYear = $('.policyYear').val();
        if (authToken && claimType && policyYear) {
            $.get('crcportal', {
                token: authToken,
                pageName: "previous_claim_details",
                claimType: claimType,
                policyYear: policyYear
            }, function(data, statusText, xhr) {
                if (xhr.status == 200) {
                    $('.previous-claim-table-body').html('');
                    if (data.prevClaimDetails) {
                        var claimDetail = data.prevClaimDetails;
                        for (var i = 0; i < claimDetail.length; i++) {
                            $('.previous-claim-table-body').append('<tr id="prevClm_' + (i + 1) + '" data-json="" data-intimationNo="' + (claimDetail[i].intimationNumber ? claimDetail[i].intimationNumber : '') + '" data-claimKey="' + (claimDetail[i].key ? claimDetail[i].key : '') + '"><td>' + (claimDetail[i].policyNumber ? claimDetail[i].policyNumber : '') + '</td><td>' +
                                (claimDetail[i].policyYear ? claimDetail[i].policyYear : '') + '</td><td>' +
                                (claimDetail[i].claimNumber ? claimDetail[i].claimNumber : '') + '</td><td>' +
                                (claimDetail[i].claimType ? claimDetail[i].claimType : '') + '</td><td>' +
                                (claimDetail[i].intimationNumber ? claimDetail[i].intimationNumber : '') + '</td> <td>' +
                                (claimDetail[i].insuredPatientName ? claimDetail[i].insuredPatientName : '') + '</td><td>' +
                                (claimDetail[i].diagnosis ? claimDetail[i].diagnosis : '') + '</td><td>' +
                                (claimDetail[i].admissionDate ? claimDetail[i].admissionDate : '') + '</td><td>' +
                                (claimDetail[i].claimStatus ? claimDetail[i].claimStatus : '') + '</td><td>' +
                                (claimDetail[i].claimAmount ? claimDetail[i].claimAmount : '') + '</td><td>' +
                                (claimDetail[i].approvedAmount ? claimDetail[i].approvedAmount : '') + '</td> <td>' +
                                (claimDetail[i].copayPercentage ? claimDetail[i].copayPercentage : '0') + '</td><td>' +
                                (claimDetail[i].hospitalName ? claimDetail[i].hospitalName : '') + '</td><td>' +
                                (claimDetail[i].pedName ? claimDetail[i].pedName : '') + '</td> <td>' +
                                (claimDetail[i].icdCodes ? claimDetail[i].icdCodes : '') + '</td><td><a class="prevClaimStatusClk hreflink" >View Claim Status</a></td><td><a class="prevDoctorViewClk hreflink" >Doctor Internal Note</a></td><td><a class="hreflink" href="' + (claimDetail[i].documentUrl ? claimDetail[i].documentUrl : "") + '" target="_blank">View Document</a></td><td><a class= "prevTrailsClk hreflink">View Trails</a></td><td><a class="prevSplTrailsClk hreflink">View Specialist Trails</a></td><td><a class="prevPedClk hreflink">View PED Details</a></td></tr>');
                        }
                    }

                    $('.claimTypeLbl').html(claimLabel);
                    if (claimType == "insured" || claimType == "risk") {
                        $('.prevPolicyYear').hide();
                        $('.policyYear').hide();
                    } else {
                        $('.prevPolicyYear').show();
                        $('.policyYear').show();
                    }
                    $("table.previous-claim-table tr:even").css("background-color", "#c2cbce");
                    $('#previous-claim-details-dialog').dialog('open');
                } else if (xhr.status == 204) {
                    alert("Details not found");
                } else if (xhr.status == 400) {
                    alert("Bad Request");
                }
                $('.inner-overlay').hide();
                $('.overlay').hide();
            });
        }
    }

    $(".fvrGradingCloseBtn, .fvrGradingCloseIcon").click(function() {
        document.getElementsByClassName('fvr-grading-modal')[0].style.display = "none";
    });

    $(".policyDetailCloseBtn").click(function() {
        $('#current_policy-details-dialog').dialog('close');
    });

    $('#preauth-details-dialog').parent('div').find('.ui-dialog-titlebar-close').click(function(e) {
        e.preventDefault();
    });

    function getUrlParameter(sParam) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : sParameterName[1];
            }
        }
    }

    jQuery(document).on("click", '.viewMedicalDetails', function() {
        $('.inner-overlay').show();
        $('.overlay').show();
        var datalink = $(this).attr('data-link');
        var rodKey = $(this).attr('data-rodKey');
        if (authToken && rodKey) {
            $.get('crcportal', {
                token: authToken,
                pageName: "previous_claim_medical_summary",
                rodKey: rodKey
            }, function(data, statusText, xhr) {
                if (xhr.status == 200) {
                    if (data) {
                        $('#medStatus').html((data.status ? data.status : ''));
                        $('#medRemarks').html((data.remarks ? data.remarks : ''));
                        $('#medAppAmt').html((data.approvedAmt ? data.approvedAmt : ''));
                        $('#medMedicalRemarks').html((data.medicalRemarks ? data.medicalRemarks : '-'));
                        $('#medDoctorNote').html((data.doctorNote ? data.doctorNote : '-'));
                        $('#medIllnessRelapse').html((data.relapseIllness ? data.relapseIllness : '-'));
                        $('#medAdmissionDate').html((data.admissionDate ? data.admissionDate : ''));
                        $('#medAdmissionReason').html((data.admissionReason ? data.admissionReason : ''));
                        $('#medDoaChange').html((data.admissionChange ? data.admissionChange : ''));
                        $('#medDays').html((data.noOfDays ? data.noOfDays : ''));
                        $('#medRoomCategory').html((data.roomCategory ? data.roomCategory : ''));
                        $('#medTreatment').html((data.natureOfTreatment ? data.natureOfTreatment : ''));
                        $('#medConsDate').html((data.consultationDate ? data.consultationDate : '-'));
                        $('#medDischargeDate').html((data.dischargeDate ? data.dischargeDate : ''));
                        $('#medCorpBuffer').html((data.isCorpBuffer == true ? '<input type="checkbox" checked />Corp Buffer' : '<input type="checkbox"/> Corp Buffer'));
                        $('#medSystemMedicine').html((data.systemMedicine ? data.systemMedicine : '-'));
                        $('#medAutoRestore').html((data.autoRestore ? data.autoRestore : ''));
                        $('#medHospDue').html((data.hospDue ? data.hospDue : ''));
                        $('#medIllness').html((data.illness ? data.illness : '-'));
                        $('#medInjuryDate').html((data.injuryDate ? data.injuryDate : '-'));
                        $('#medPatientStatus').html((data.patientStatus ? data.patientStatus : ''));
                        $('#medMedicoLegal').html((data.medicoLegalCase == true ? '<input type="radio" disabled name="medFld" value="YES" checked />YES <input type="radio" name="medFld" value="NO" disabled />NO' : '<input type="radio" name="medFld" value="YES" disabled  />YES <input type="radio" name="medFld" value="NO" disabled checked />NO'));
                        $('#medInjuryCause').html((data.injuryCause ? data.injuryCause : ''));
                        $('#medPoliceReported').html((data.reportedToPolice == true ? '<input type="radio" disabled name="policeFld" value="YES" checked />YES <input type="radio" name="policeFld" value="NO" disabled />NO' : '<input type="radio" name="policeFld" value="YES" disabled  />YES <input type="radio" name="policeFld" value="NO" disabled checked />NO'));
                        $('#medTreatmentType').html((data.treatmentType ? data.treatmentType : ''));
                        $('#medBenefits').html((data.benefits ? data.benefits : ''));
                        $('#medIsAccident').html((data.isAccident == true ? '<input type="radio" disabled name="accFld" value="YES" checked />Accident <input type="radio" name="accFld" value="NO" disabled />Death' : '<input type="radio" name="accFld" value="YES" disabled  />Accident <input type="radio" name="accFld" value="NO" disabled checked />Death'));
                        $('#medAccidentDate').html((data.accidentDate ? data.accidentDate : ''));
                        $('.medical-benefit-tbody').html('');
                        $('.medical-procedure-tbody').html('');
                        $('.medical-diagnosis-tbody').html('');
                        if (data.benefitList) {
                            var benefits = data.benefitList;
                            for (var i = 0; i < benefits.length; i++) {
                                $('.medical-benefit-tbody').append('<tr><td>' + (benefits[i].benefitCover ? benefits[i].benefitCover : '') + '</td><td>' + (benefits[i].percentage ? benefits[i].percentage : '') + '</td><td>' + (benefits[i].sumInsured ? addCommas(benefits[i].sumInsured) : '') + '</td><td>' + (benefits[i].eligibleAmount ? addCommas(benefits[i].eligibleAmount) : '') + '</td></tr>');
                            }
                        }
                        if (data.procedureList) {
                            var procedure = data.procedureList;
                            for (var i = 0; i < procedure.length; i++) {
                                $('.medical-procedure-tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (procedure[i].procedureNameValue ? procedure[i].procedureNameValue : '') + '</td><td>' + (procedure[i].procedureCodeValue ? procedure[i].procedureCodeValue : '') + '</td><td>' + (procedure[i].pakageRate ? procedure[i].pakageRate : '') + '</td><td>' + (procedure[i].dayCareProcedure ? procedure[i].dayCareProcedure : '') + '</td><td>' + (procedure[i].considerForDayCareValue ? procedure[i].considerForDayCareValue : '') + '</td><td>' + (procedure[i].subLimitApplicable ? procedure[i].subLimitApplicable : '') + '</td><td>' + (procedure[i].sublimitNameValue ? procedure[i].sublimitNameValue : '') + '</td><td>' + (procedure[i].subLimitAmount ? procedure[i].subLimitAmount : '') + '</td><td>' + (procedure[i].considerForPaymentValue ? procedure[i].considerForPaymentValue : '') + '</td><td>' + (procedure[i].remarks ? procedure[i].remarks : '') + '</td></tr>');
                            }
                        }
                        if (data.diagnosisList) {
                            var diagnosis = data.diagnosisList;
                            for (var i = 0; i < diagnosis.length; i++) {
                                $('.medical-diagnosis-tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (diagnosis[i].diagnosis ? diagnosis[i].diagnosis : '') + '</td><td>' + (diagnosis[i].icdChapterValue ? diagnosis[i].icdChapterValue : '') + '</td><td>' + (diagnosis[i].icdBlockValue ? diagnosis[i].icdBlockValue : '') + '</td><td>' + (diagnosis[i].icdCodeValue ? diagnosis[i].icdCodeValue : '') + '</td><td>' + (diagnosis[i].sublimitApplicableValue ? diagnosis[i].sublimitApplicableValue : '') + '</td><td>' + (diagnosis[i].sublimitNameValue ? diagnosis[i].sublimitNameValue : '') + '</td><td>' + (diagnosis[i].sublimitAmt ? diagnosis[i].sublimitAmt : '') + '</td><td>' + (diagnosis[i].exclusionDetails ? diagnosis[i].exclusionDetails.value : '') + '</td><td>' + (diagnosis[i].exclusionDetailsValue ? diagnosis[i].exclusionDetailsValue : '') + '</td><td>' + (diagnosis[i].coPayValue ? diagnosis[i].coPayValue : '') + '</td><td>' + (diagnosis[i].considerForPaymentValue ? diagnosis[i].considerForPaymentValue : '') + '</td><td>' + (diagnosis[i].sumInsuredRestrictionValue ? diagnosis[i].sumInsuredRestrictionValue : '') + '</td><td>' + (diagnosis[i].diagnosisRemarks ? diagnosis[i].diagnosisRemarks : '') + '</td></tr>');
                            }
                        }
                        if (data.speciality) {
                            var speciality = data.speciality;
                            for (var i = 0; i < speciality.length; i++) {
                                $('.medical-Speciality-tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (speciality[i].specialityType ? speciality[i].specialityType.value : '') + '</td><td>' + (speciality[i].remarks ? addCommas(speciality[i].remarks) : '') + '</td></tr>');
                            }
                        }

                        $('.medical-optional-cover-tbody').html('');
                        if (data.optionalCoversList) {
                            var optionalCover = data.optionalCoversList;
                            for (var i = 0; i < optionalCover.length; i++) {
                                $('.medical-optional-cover-tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (optionalCover[i].cover ? optionalCover[i].cover : '') + '</td><td>' + (optionalCover[i].claimedAmt ? optionalCover[i].claimedAmt : '') + '</td></tr>');
                            }
                        }

                        $('.medical-addon-cover-tbody').html('');
                        if (data.addOnCoversList) {
                            var addonCover = data.addOnCoversList;
                            for (var i = 0; i < addonCover.length; i++) {
                                $('.medical-addon-cover-tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (addonCover[i].cover ? addonCover[i].cover : '') + '</td><td>' + (addonCover[i].claimedAmt ? addonCover[i].claimedAmt : '') + '</td></tr>');
                            }
                        }

                        $('.medical-verification-tbody').html('');
                        if (data.medicalVerification) {
                            var medicalVerification = data.medicalVerification;
                            for (var i = 0; i < medicalVerification.length; i++) {
                                $('.medical-verification-tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (medicalVerification[i].description ? medicalVerification[i].description : '') + '</td><td>' + (medicalVerification[i].verifiedFlag ? medicalVerification[i].verifiedFlag : '') + '</td><td>' + (medicalVerification[i].remarks ? medicalVerification[i].remarks : '') + '</td></tr>');
                            }
                        }

                        $('.medical-PED-tbody').html('');
                        if (data.pedValidationList) {
                            var pedValidationList = data.pedValidationList;
                            for (var i = 0; i < pedValidationList.length; i++) {
                                $('.medical-PED-tbody').append('<tr><td>' + (pedValidationList[i].diagnosisName ? pedValidationList[i].diagnosisName : '') + '</td><td>' + (pedValidationList[i].pedName ? pedValidationList[i].pedName : '') + '</td><td>' + (pedValidationList[i].policyAgeing ? pedValidationList[i].policyAgeing : '') + '</td><td>' + (pedValidationList[i].impactOnDiagnosisValue ? pedValidationList[i].impactOnDiagnosisValue : '') + '</td><td>' + (pedValidationList[i].exclusionDetailsValue ? pedValidationList[i].exclusionDetailsValue : '') + '</td><td>' + (pedValidationList[i].coPayValue ? pedValidationList[i].coPayValue : '') + '</td><td>' + (pedValidationList[i].remarks ? pedValidationList[i].remarks : '') + '</td></tr>');
                            }
                        }

                        $('.medical-exclusion-tbody').html('');
                        if (data.procedureExclusionList) {
                            var procedureExclusionList = data.procedureExclusionList;
                            for (var i = 0; i < procedureExclusionList.length; i++) {
                                $('.medical-exclusion-tbody').append('<tr><td>' + (procedureExclusionList[i].procedureNameValue ? procedureExclusionList[i].procedureNameValue : '') + '</td><td>' + (procedureExclusionList[i].procedureCodeValue ? procedureExclusionList[i].procedureCodeValue : '') + '</td><td>' + (procedureExclusionList[i].packageAmount ? procedureExclusionList[i].packageAmount : '') + '</td><td>' + (procedureExclusionList[i].policyAging ? procedureExclusionList[i].policyAging : '') + '</td><td>' + (procedureExclusionList[i].procedureStatus ? procedureExclusionList[i].procedureStatus.value : '') + '</td><td>' + (procedureExclusionList[i].exclusionDetails ? procedureExclusionList[i].exclusionDetails.value : '') + '</td><td>' + (procedureExclusionList[i].remarks ? procedureExclusionList[i].remarks : '') + '</td></tr>');
                            }
                        }

                        $('#med-invest-report').html((data.status == "true" ? '<input type="checkbox" checked /><span style="padding-left: 3%;">Investigation Report Reviewed</span>' : '<input type="checkbox"  /><span style="padding-left: 3%;">Investigation Report Reviewed</span>'));
                        $('#med-invest-name').html((data.investigatorName ? data.investigatorName : '-'));
                        $('#med-invest-remarks').html((data.investigatorRemarks ? data.investigatorRemarks : '-'));

                    }
                    document.getElementsByClassName('medicalDetailsModal')[0].style.display = "block";
                    if (datalink == "subPage") {
                        $('.previous_claim_status_dialog').css('display', 'none');
                        $('.medicalCloseIcon').addClass('temp');
                    }
                } else if (xhr.status == 204) {
                    alert("Details not found");
                } else if (xhr.status == 400) {
                    alert("Bad Request");
                }
                $('.inner-overlay').hide();
                $('.overlay').hide();
            });
        }
        return false;
    });

    function formFVRDocumentModalData(docDetails) {
        $('#intimationNo').text((docDetails.intimationNo ? docDetails.intimationNo : ''));
        $('#claimNo').text((docDetails.claimNo ? docDetails.claimNo : ''));
        $('.fvrDocumentBody').html('');
        var documents = docDetails.dmsDocumentDetailsDTOList;
        if (documents) {
            for (var i = 0; i < documents.length; i++) {
                $('.fvrDocumentBody').append('<tr><td>' + (i + 1) + '</td><td>' + (documents[i].documentType ? documents[i].documentType : '') + '</td><td>' + (documents[i].cashlessOrReimbursement ? documents[i].cashlessOrReimbursement : '') + '</td><td style="width: 10%">' + (documents[i].fileName ? documents[i].fileName : '') + '</td><td>' + (documents[i].documentCreatedDateValue ? documents[i].documentCreatedDateValue : '') + '</td><td>' + (documents[i].documentSource ? documents[i].documentSource : '') + '</td><td><a class="fvrViewDocumentDetailBtn"  data-fileName="' + (documents[i].fileName ? documents[i].fileName : '') + '" data-fileToken="' + (documents[i].dmsDocToken ? documents[i].dmsDocToken : '') + '">View Document </a></td></tr>');
            }
        }
        document.getElementsByClassName('fvr-document-modal')[0].style.display = "block";
    }

    $(".fvrDocumentCloseBtn, .fvrDocumentCloseIcon").click(function() {
        document.getElementsByClassName('fvr-document-modal')[0].style.display = "none";
        if ($(this).hasClass('viaPrevQryclk')) {
            $(this).removeClass('viaPrevQryclk');
            $('.ackQryDetailsModal').removeClass('hidden');
            $('.prev_claim_query_modal').removeClass('hidden');
            $('.previous_claim_status_dialog').removeClass('hidden');
        } else {
            $('.acknowledge-details-dialog').removeClass('hidden');
            $('.ackQryDetailsModal').removeClass('hidden');
        }
    });

    $(".64VBDocumentViewCloseIcon, .64VBDocumentViewCloseBtn").click(function() {
        $('canvas').remove();
        document.getElementsByClassName('64VBDocumentViewModal')[0].style.display = "none";
    });

    $(".insuredDetailCloseIcon").click(function() {
        document.getElementsByClassName('insuredDetailModal')[0].style.display = "none";
    });

    $(".currentPolicySchedule").click(function() {
        var url = $(this).attr('data-url');
        if (url) {
            window.open(url, "popUp", "resizable=yes");
        }
    });

    $(".fvrDocumentViewCloseIcon, .fvrDocumentViewCloseBtn").click(function() {
        document.getElementsByClassName('fvrDocumentViewModal')[0].style.display = "none";
        if ($(this).hasClass('temp')) {
            $('.coorViewFileModal').removeClass('hidden');
            if ($(this).hasClass('coordinator')) {
                $('.coordinator-reply-dialog').removeClass('hidden');
                $(this).removeClass('coordinator');
            } else {
                $('.specialist-trail-dialog').removeClass('hidden');
                $(this).removeClass('specialist');
            }
            $(this).removeClass('temp');
        }
        $('.frame-title').html('View Uploaded Document');
    });

    // View PED Details
    jQuery(document).on("click", '.pedViewBtn', function() {
        var pedDetails = JSON.parse($(this).closest('tr').attr('data-json'));
        $('.pedDetailBody').html('');
        if (pedDetails) {
            var repLetterDate = (pedDetails.repLetterDate ? pedDetails.repLetterDate : '');
            var requestedDate = (pedDetails.requestedDate ? pedDetails.requestedDate : '');
            $('.viewPedSug').html(pedDetails.pedSuggestionName ? pedDetails.pedSuggestionName : '');
            $('.viewPedName').html(pedDetails.pedName ? pedDetails.pedName : '');
            $('.viewRepdate').html(pedDetails.repLetterDate ? pedDetails.repLetterDate : '');
            $('.viewPedRemarks').html(pedDetails.remarks ? pedDetails.remarks : '');
            // View PED Endorsement Details
            var pedEndorse = pedDetails.viewPEDEndoresementDetailsDTO;
            for (var i = 0; i < pedEndorse.length; i++) {
                $('.pedDetailBody').append('<tr><td>' + (pedEndorse[i].pedCode ? pedEndorse[i].pedCode : '') + '</td><td>' + (pedEndorse[i].description ? pedEndorse[i].description : '') + '</td><td style="width: 10%">' + (pedEndorse[i].icdChapter ? pedEndorse[i].icdChapter : '') + '</td><td>' + (pedEndorse[i].icdBlock ? pedEndorse[i].icdBlock : '') + '</td><td>' + (pedEndorse[i].icdCode ? pedEndorse[i].icdCode : '') + '</td><td>' + (pedEndorse[i].source ? pedEndorse[i].source : '') + '</td><td>' + (pedEndorse[i].othersSpecify ? pedEndorse[i].othersSpecify : '') + '</td><td>' + (pedEndorse[i].doctorRemarks ? pedEndorse[i].doctorRemarks : '') + '</td></tr>');
            }

            if (pedDetails.hasPedSpecialistDetails) {
                $('.viewPedStatusSpl').html((pedDetails.pedSpecialistStatus ? pedDetails.pedSpecialistStatus : ''));
                $('.viewPedRemarksSpl').html((pedDetails.pedSpecialistRemarks ? pedDetails.pedSpecialistRemarks : ''));
                $('.ped-specialist-div').removeClass('hidden');
                $('.ped-specialist-empty-div').addClass('hidden');
            } else {
                $('.ped-specialist-div').addClass('hidden');
                $('.ped-specialist-empty-div').removeClass('hidden');
            }

            if (pedDetails.pedDiscussedWith) {
                $('.discussedWith').show();
                $('.viewDiscussedWith').html(pedDetails.pedDiscussedWith);
            } else {
                $('.discussedWith').hide();
            }

            if (pedDetails.pedSuggestion) {
                $('.suggestion').show();
                $('.viewSuggestion').html(pedDetails.pedSuggestion);
            } else {
                $('.suggestion').hide();
            }

            $('.viewReqId').html((pedDetails.pedRequestorId ? pedDetails.pedRequestorId : ''));
            $('.viewReqDate').html((pedDetails.pedRequestorDate ? pedDetails.pedRequestorDate : ''));

        } else {
            $('.pedDetailBody').append('<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>');
        }

        // View PED Ammended Details
        if ($(this).closest('tr').attr('data-ammended')) {
            var pedAmmendedDetails = JSON.parse($(this).closest('tr').attr('data-ammended'));

            $('.viewPedSugAmd').val((pedAmmendedDetails.pedSuggestionName ? pedAmmendedDetails.pedSuggestionName : ''));
            $('.viewPedNameAmd').val((pedAmmendedDetails.pedName ? pedAmmendedDetails.pedName : ''));
            $('.viewRepdateAmd').val(repLetterDate);
            $('.viewPedRemarksAmd').val((pedAmmendedDetails.remarks ? pedAmmendedDetails.remarks : ''));
            $('.viewReqIdAmd').val((pedAmmendedDetails.requestorId ? pedAmmendedDetails.requestorId : ''));
            $('.viewReqDateAmd').val(requestedDate);
            var endorseDetails = pedAmmendedDetails.viewPEDEndoresementDetailsDTO;
            $('.ped-ammended-table-body').html('');
            for (var i = 0; i < endorseDetails.length; i++) {
                $('.ped-ammended-table-body').append('<tr><td>' + (endorseDetails[i].description ? endorseDetails[i].description : '') + '</td><td>' + (endorseDetails[i].pedCode ? endorseDetails[i].pedCode : '') + '</td><td style="width: 10%">' + (endorseDetails[i].icdChapter ? endorseDetails[i].icdChapter : '') + '</td><td>' + (endorseDetails[i].icdBlock ? endorseDetails[i].icdBlock : '') + '</td><td>' + (endorseDetails[i].icdCode ? endorseDetails[i].icdCode : '') + '</td><td>' + (endorseDetails[i].source ? endorseDetails[i].source : '') + '</td><td>' + (endorseDetails[i].othersSpecify ? endorseDetails[i].othersSpecify : '') + '</td><td>' + (endorseDetails[i].doctorRemarks ? endorseDetails[i].doctorRemarks : '') + '</td></tr>');
            }
            $('.ped-ammended-div').removeClass('hidden');
        } else {
            $('.ped-ammended-div').addClass('hidden');
        }

        // View PED Specialist Details
        if ($(this).attr('data-add-ped')) {
            var pedAdditionalDetails = JSON.parse($(this).attr('data-add-ped'));
            if (pedAdditionalDetails.hasPedSpecialistDetails) {
                $('.viewPedStatusSpl').html((pedAdditionalDetails.pedSpecialistStatus ? pedAdditionalDetails.pedSpecialistStatus : ''));
                $('.viewPedRemarksSpl').html((pedAdditionalDetails.pedSpecialistRemarks ? pedAdditionalDetails.pedSpecialistRemarks : ''));
                $('.ped-specialist-div').removeClass('hidden');
                $('.ped-specialist-empty-div').addClass('hidden');
            } else {
                $('.ped-specialist-div').addClass('hidden');
                $('.ped-specialist-empty-div').removeClass('hidden');
            }
        }

        // View PED Approved Details
        if ($(this).closest('tr').attr('data-approved')) {
            var pedApprovedDetails = JSON.parse($(this).closest('tr').attr('data-approved'));

            $('.viewPedSugApp').val((pedApprovedDetails.pedSuggestionName ? pedApprovedDetails.pedSuggestionName : ''));
            $('.viewPedNameApp').val((pedApprovedDetails.pedName ? pedApprovedDetails.pedName : ''));
            $('.viewRepdateApp').val(repLetterDate);
            $('.viewPedRemarksApp').val((pedApprovedDetails.remarks ? pedApprovedDetails.remarks : ''));

            var endorseDetails = pedApprovedDetails.viewPEDEndoresementDetailsDTO;
            $('.ped-approved-table-body').html('');
            for (var i = 0; i < endorseDetails.length; i++) {
                $('.ped-approved-table-body').append('<tr><td>' + (endorseDetails[i].description ? endorseDetails[i].description : '') + '</td><td>' + (endorseDetails[i].pedCode ? endorseDetails[i].pedCode : '') + '</td><td style="width: 10%">' + (endorseDetails[i].icdChapter ? endorseDetails[i].icdChapter : '') + '</td><td>' + (endorseDetails[i].icdBlock ? endorseDetails[i].icdBlock : '') + '</td><td>' + (endorseDetails[i].icdCode ? endorseDetails[i].icdCode : '') + '</td><td>' + (endorseDetails[i].source ? endorseDetails[i].source : '') + '</td><td>' + (endorseDetails[i].othersSpecify ? endorseDetails[i].othersSpecify : '') + '</td><td>' + (endorseDetails[i].doctorRemarks ? endorseDetails[i].doctorRemarks : '') + '</td></tr>');
            }

            $('.viewPedStatusApp').val((pedApprovedDetails.pedSuggestionName ? pedApprovedDetails.pedSuggestionName : ''));
            $('.viewQueryApp').val((pedApprovedDetails.queryRemarks ? pedApprovedDetails.queryRemarks : ''));
            $('.viewSplApp').val((pedApprovedDetails.specialistType ? pedApprovedDetails.specialistType : ''));
            $('.viewReasonApp').val((pedApprovedDetails.reasonforReferring ? pedApprovedDetails.reasonforReferring : ''));
            $('.viewRejectionApp').val((pedApprovedDetails.rejectionRemarks ? pedApprovedDetails.rejectionRemarks : ''));
            $('.viewApprovalApp').val((pedApprovedDetails.approvalRemarks ? pedApprovedDetails.approvalRemarks : ''));


            $('.ped-approved-div').removeClass('hidden');
        } else {
            $('.ped-approved-div').addClass('hidden');
        }

        document.getElementsByClassName('ped-detail-modal')[0].style.display = "block";

    });

    jQuery(document).on("click", '.investigatorViewBtn', function() {
        $('.inner-overlay').show();
        $('.overlay').show();
        var investigationKey = $(this).attr('data-key');
        var investigationAssignedKey = $(this).attr('data-assigned-key');
        $('.investigationDetailHistoryBtn').attr('data-json', $(this).closest('tr').attr('data-json'));
        if (investigationKey && investigationAssignedKey && authToken) {
            $.get('crcportal', {
                token: authToken,
                pageName: "investigation_detailed_report",
                investigationKey: investigationKey,
                investigationAssignedKey: investigationAssignedKey
            }, function(data, statusText, xhr) {
                if (xhr.status == 200) {
                    if (data) {
                        $('.invRequestRole').val((data.invRequestRole != null ? data.invRequestRole : ""));
                        $('.invAllocationTo').val((data.invAllocationTo != null ? data.invAllocationTo : ""));
                        $('.invRequestId').val((data.invRequestId != null ? data.invRequestId : ""));
                        $('.invReason').val((data.invReason != null ? data.invReason : ""));
                        $('.invTriggerPoints').val((data.invTriggerPoints != null ? data.invTriggerPoints : ""));
                        $('.invRequestDate').val((data.invRequestDate != null ? data.invRequestDate : ""));

                        $('.invApproverId').val((data.invApproverId != null ? data.invApproverId : ""));
                        $('.invApprovedRemarks').val((data.invApprovedRemarks != null ? data.invApprovedRemarks : ""));
                        $('.invProcessedDate').val((data.invProcessedDate != null ? data.invProcessedDate : ""));
                        $('.invOutcome').val((data.invOutcome != null ? data.invOutcome : ""));

                        if (data.hasDraftDetails == true) {
                            $('.invDraftPersonId').val((data.invDraftPersonId != null ? data.invDraftPersonId : ""));
                            $('.invDraftProcessedDate').val((data.invDraftProcessedDate != null ? data.invDraftProcessedDate : ""));
                            $('.invDraftAllocationTo').val((data.invDraftAllocationTo != null ? data.invDraftAllocationTo : ""));
                            $('.invDraftFacts').val((data.invDraftFacts != null ? data.invDraftFacts : ""));
                            $('.invDraftClmBck').val((data.invDraftClmBck != null ? data.invDraftClmBck : ""));

                            if (data.draftTableList) {
                                var list = data.draftTableList;
                                $('.inv-draft-table-body').html('');
                                for (var i = 0; i < list.length; i++) {
                                    $('.inv-draft-table-body').append('<tr><td>' + (i + 1) + '</td><td>' + (list[i].remarks ? list[i].remarks : '') + '</td></tr>');
                                }
                            }

                            $('.invDraftDiv').removeClass('hidden');
                        } else {
                            $('.invDraftDiv').addClass('hidden');
                        }

                        if (data.hasAssignDetails == true) {
                            $('.invAssignId').val((data.invAssignId != null ? data.invAssignId : ""));
                            $('.invAssignedDate').val((data.invAssignedDate != null ? data.invAssignedDate : ""));

                            if (data.assignInvestigatorList) {
                                var list = data.assignInvestigatorList;
                                $('.inv-assign-table-body').html('');
                                for (var i = 0; i < list.length; i++) {
                                    $('.inv-assign-table-body').append('<tr><td>' + (i + 1) + '</td><td>' + (list[i].stateSelectValue ? list[i].stateSelectValue.value : '') + '</td><td style="width: 10%">' + (list[i].citySelectValue ? list[i].citySelectValue.value : '') + '</td><td>' + (list[i].allocationToSelectValue ? list[i].allocationToSelectValue.value : '') + '</td><td>' + (list[i].investigatorNameListSelectValue ? list[i].investigatorNameListSelectValue.value : '') + '</td><td>' + (list[i].investigatorTelNo ? list[i].investigatorTelNo : '') + '</td><td>' + (list[i].investigatorMobileNo ? list[i].investigatorMobileNo : '') + '</td></tr>');
                                }
                            }
                            $('.invAssignDiv').removeClass('hidden');
                        } else {
                            $('.invAssignDiv').addClass('hidden');
                        }

                        if (data.hasUploadedDetails == true) {
                            $('.invUploadedId').val((data.invUploadedId != null ? data.invUploadedId : ""));
                            $('.invUploadCompletionDate').val((data.invUploadCompletionDate != null ? data.invUploadCompletionDate : ""));
                            $('.invFileName').val((data.invFileName != null ? data.invFileName : ""));
                            $('.invFileType').val((data.invFileType != null ? data.invFileType : ""));
                            $('.invUploadCompletionDate').val((data.invUploadCompletionDate != null ? data.invUploadCompletionDate : ""));
                            $('.invCompletionRemarks').val((data.invCompletionRemarks != null ? data.invCompletionRemarks : ""));
                            $('.invUploadDiv').removeClass('hidden');
                        } else {
                            $('.invUploadDiv').addClass('hidden');
                        }

                        if (data.hasGradedDetails == true) {
                            $('.invGradeId').val((data.invGradeId != null ? data.invGradeId : ""));
                            $('.invGradeDate').val((data.invGradeDate != null ? data.invGradeDate : ""));
                            $('.invGradeCategory').val((data.invGradeCategory != null ? data.invGradeCategory : ""));
                            $('.invGradeRemarks').val((data.invGradeRemarks != null ? data.invGradeRemarks : ""));
                            $('.invGradingDiv').removeClass('hidden');
                        } else {
                            $('.invGradingDiv').addClass('hidden');
                        }


                    }
                    document.getElementsByClassName('investigation-detail-modal')[0].style.display = "block";
                } else if (xhr.status == 204) {
                    alert("Details not found");
                } else if (xhr.status == 400) {
                    alert("Bad Request");
                }
                $('.inner-overlay').hide();
                $('.overlay').hide();
            });
        }
        return false;
    });

    $(".pedDetailCloseIcon").click(function() {
        document.getElementsByClassName('ped-detail-modal')[0].style.display = "none";
    });

    // View PED Trail Details
    jQuery(document).on("click", '.pedHistoryBtn', function() {
        var pedHistory = JSON.parse($(this).closest('tr').attr('data-history'));
        if (pedHistory) {
            $('.pedHistoryBody').html('');
            for (var i = 0; i < pedHistory.length; i++) {
                $('.pedHistoryBody').append('<tr><td>' + (pedHistory[i].status ? pedHistory[i].status : '') + '</td><td>' + (pedHistory[i].strDateAndTime ? pedHistory[i].strDateAndTime : '') + '</td><td style="width: 10%">' + (pedHistory[i].userName ? pedHistory[i].userName : '') + '</td><td>' + (pedHistory[i].remarks ? pedHistory[i].remarks : '') + '</td></tr>');
            }
            document.getElementsByClassName('pedHistoryModal')[0].style.display = "block";
        }
    });

    $(".pedHistoryCloseIcon").click(function() {
        document.getElementsByClassName('pedHistoryModal')[0].style.display = "none";
    });

    $(".investHistoryCloseIcon").click(function() {
        document.getElementsByClassName('investHistoryModal')[0].style.display = "none";
    });

    jQuery(document).on("click", '.investigationDetailCloseIcon', function() {
        document.getElementsByClassName('investigation-detail-modal')[0].style.display = "none";
    });

    $(".ackQryDetailCloseBtn").click(function() {
        document.getElementsByClassName('ackQryDetailsModal')[0].style.display = "none";
    });

    $(".rejectionDetailCloseBtn").click(function() {
        document.getElementsByClassName('rejectionDetailsModal')[0].style.display = "none";
        if ($(this).hasClass('temp')) {
            $('.previous_claim_status_dialog').css('display', 'block');
            $(this).removeClass('temp');
        }
    });

    $(".coorViewFileCloseIcon").click(function() {
        document.getElementsByClassName('coorViewFileModal')[0].style.display = "none";
    });

    $(".prevAckCloseIcon").click(function() {
        document.getElementsByClassName('prevAcknowledgementDialog')[0].style.display = "none";
        if ($(this).hasClass('temp')) {
            $('.previous_claim_status_dialog').css('display', 'block');
            $(this).removeClass('temp');
        }
    });

    $(".billDetailsCloseIcon").click(function() {
        if ($(this).hasClass('prevBillClicked')) {
            document.getElementsByClassName('previous_claim_status_dialog')[0].style.display = "block";
            $(this).removeClass('prevBillClicked');
        }
        document.getElementsByClassName('bill-details-modal')[0].style.display = "none";
    });

    $('#ackViewQryLetter').click(function() {
        var docDetails = JSON.parse($(this).attr('data-dms'));
        if (docDetails) {
            formFVRDocumentModalData(docDetails);
            if ($(this).hasClass('viaPrevQryclk')) {
                $(".fvrDocumentCloseBtn, .fvrDocumentCloseIcon").addClass('viaPrevQryclk');
                $('.ackQryDetailsModal').addClass('hidden');
                $('.prev_claim_query_modal').addClass('hidden');
                $('.previous_claim_status_dialog').addClass('hidden');
                $(this).removeClass('viaPrevQryclk');
            } else {
                $('.acknowledge-details-dialog').addClass('hidden');
                $('.ackQryDetailsModal').addClass('hidden');
            }
        }
    });

    $('#detailedView').click(function() {

        $('.preauthTitleText').val($(".preselect option:selected").text());
        $('.preauthTitleText').removeClass('hidden');
        $('.preselect').addClass('hidden');
        $('.detailClose').removeClass('hidden');
        $('.preauth-sublimit').removeClass('hidden');
        $('#detailedView').addClass('hidden');
        $('.preauth-pedvalidation').removeClass('hidden');
        $('.preauth-exclusion').removeClass('hidden');
        //$('.preauth-additional-fields').removeClass('hidden');
        $('.preauth-speciality').removeClass('hidden');
    });

    $('#medicalDetailBtn').click(function() {
        $('.medicalDetailCloseIcon').removeClass('hidden');
        $('.medicalCloseIcon').addClass('hidden');
        $('.medical-detail-view').removeClass('hidden');
        $('.medicalBenefitsTable').addClass('hidden');
        $('.medicalOptionalCoverTable').addClass('hidden');
        $('.medicalAddonCoverTable').addClass('hidden');
        $('#medicalDetailBtn').addClass('hidden');
    });

    $('.detailClose').click(function() {

        $('.preauthTitleText').addClass('hidden');
        $('.preselect').removeClass('hidden');
        $('.detailClose').addClass('hidden');
        $('.preauth-sublimit').addClass('hidden');
        $('#detailedView').removeClass('hidden');
        $('.preauth-pedvalidation').addClass('hidden');
        $('.preauth-exclusion').addClass('hidden');
        //$('.preauth-additional-fields').addClass('hidden');
        $('.preauth-speciality').addClass('hidden');
    });

    $('.medicalDetailCloseIcon').click(function() {
        $('.medicalDetailCloseIcon').addClass('hidden');
        $('.medicalCloseIcon').removeClass('hidden');
        $('.medical-detail-view').addClass('hidden');
        $('.medicalBenefitsTable').removeClass('hidden');
        $('.medicalOptionalCoverTable').removeClass('hidden');
        $('.medicalAddonCoverTable').removeClass('hidden');
        $('#medicalDetailBtn').removeClass('hidden');
    });

    $('.medicalCloseIcon').click(function() {
        document.getElementsByClassName('medicalDetailsModal')[0].style.display = "none";
        if ($(this).hasClass('temp')) {
            $('.previous_claim_status_dialog').css('display', 'block');
            $(this).removeClass('temp');
        }
    });

    $('#viewBillDetails').click(function() {
        var billDetails = JSON.parse($(this).attr('data-json'));
        var netClaimAmount = $(this).attr('data-claimAmt');
        var netAmount = $(this).attr('data-amt');
        var netNonpayableAmount = $(this).attr('data-nonpayable');
        var netPayableAmount = $(this).attr('data-payable');

        if (billDetails) {
            $('.bill-details-tbody').html('');
            for (var i = 0; i < billDetails.length; i++) {
                $('.bill-details-tbody').append('<tr><td>' + +(i + 1) + '</td><td>' + (billDetails[i].rodNumber ? billDetails[i].rodNumber : '') + '</td><td>' + (billDetails[i].fileType ? billDetails[i].fileType : '') + '</td><td>' + (billDetails[i].fileName ? billDetails[i].fileName : '') + '</td><td>' + (billDetails[i].billNumber ? billDetails[i].billNumber : '') + '</td><td>' + (billDetails[i].billDate ? billDetails[i].billDate : '') + '</td><td>' + (billDetails[i].noOfItems ? billDetails[i].noOfItems : '') + '</td><td>' + (billDetails[i].billValue ? billDetails[i].billValue : '') + '</td><td>' + (billDetails[i].itemNo ? billDetails[i].itemNo : '') + '</td><td>' + (billDetails[i].itemName ? billDetails[i].itemName : '') + '</td><td>' + (billDetails[i].classification ? billDetails[i].classification : '') + '</td><td>' + (billDetails[i].category ? billDetails[i].category : '') + '</td><td>' + (billDetails[i].noOfDays ? billDetails[i].noOfDays : '') + '</td><td>' + (billDetails[i].perDayAmt ? billDetails[i].perDayAmt : '') + '</td><td>' + (billDetails[i].claimedAmount ? billDetails[i].claimedAmount : '') + '</td><td>' + (billDetails[i].entitlementNoOfDays ? billDetails[i].entitlementNoOfDays : '') + '</td><td>' + (billDetails[i].entitlementPerDayAmt ? billDetails[i].entitlementPerDayAmt : '') + '</td><td>' + (billDetails[i].amount ? billDetails[i].amount : '') + '</td><td>' + (billDetails[i].deductionNonPayableAmount ? billDetails[i].deductionNonPayableAmount : '') + '</td><td>' + (billDetails[i].payableAmount ? billDetails[i].payableAmount : '') + '</td><td>' + (billDetails[i].reason ? billDetails[i].reason : '') + '</td><td>' + (billDetails[i].medicalRemarks ? billDetails[i].medicalRemarks : '') + '</td><td>' + (billDetails[i].irdaLevel1 ? billDetails[i].irdaLevel1 : '') + '</td><td>' + (billDetails[i].irdaLevel2 ? billDetails[i].irdaLevel2 : '') + '</td><td>' + (billDetails[i].irdaLevel3 ? billDetails[i].irdaLevel3 : '') + '</td></tr>');
            }
            $("table.billdetail_table tr:even").css("background-color", "#c2cbce");
            $('.bill-details-tbody').append('<tr class="subtotal"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>' + (netClaimAmount ? netClaimAmount : '') + '</td><td></td><td></td><td>' + (netAmount ? netAmount : '') + '</td><td>' + (netNonpayableAmount ? netNonpayableAmount : '') + '</td><td>' + (netPayableAmount ? netPayableAmount : '') + '</td><td></td><td></td><td></td><td></td><td></td></tr>');
            document.getElementsByClassName('bill-details-modal')[0].style.display = "block";
        }
    });

    $('.preselect').on('change', function() {

        $('.overlay').show();
        var preauthKey = $(this).val();
        if (preauthKey && authToken) {
            $.get('crcportal', {
                token: authToken,
                pageName: "pre_auth_details",
                preIntimationKey: preauthKey
            }, function(data, statusText, xhr) {
                if (xhr.status == 200) {
                    getPreAuthDetails(data.preAuthDetails);
                    $('#preauth-details-dialog').dialog('open');
                } else if (xhr.status == 204) {
                    alert("Details not found");
                } else if (xhr.status == 400) {
                    alert("Bad Request");
                }
                $('.overlay').hide();
            });
        }
    });

    // View FVR Grading Details
    jQuery(document).on("click", '.fvrGradingBtn', function() {

        var fvrDetails = JSON.parse($(this).closest('tr').attr('data-grading'));
        $('.fvrSeqValue').html("FVR Sequence " + ordinal_suffix_of(fvrDetails.serialNumber) + " FVR");
        $('#repCode').val((fvrDetails.representativeCode ? fvrDetails.representativeCode : ''));
        $('#repName').val((fvrDetails.representativeName ? fvrDetails.representativeName : ''));
        $('#fvrAssignedDate').val((fvrDetails.fvrassignedDate ? fvrDetails.fvrassignedDate : ''));
        $('#fvrReceivedDate').val((fvrDetails.fvrReceivedDate ? fvrDetails.fvrReceivedDate : ''));
        $('#fvrTat').val((fvrDetails.fVRTAT ? fvrDetails.fVRTAT : '0'));
        var gradingSegmentDetails = fvrDetails.newFvrGradingDTO;
        $('.fvrSegA').html('');
        $('.fvrSegB').html('');
        $('.fvrSegC').html('');
        if (gradingSegmentDetails) {
            for (var i = 0; i < gradingSegmentDetails.length; i++) {
                if (gradingSegmentDetails[i].statusFlagSegmentA != null && gradingSegmentDetails[i].statusFlagSegmentA == 'Y') {
                    $('.fvrSegA').append('<tr><td>' + (i + 1) + '</td><td>' + (gradingSegmentDetails[i].category ? gradingSegmentDetails[i].category : '') + '</td><td><input disabled type="radio"' + (gradingSegmentDetails[i].statusFlagSegmentA == 'Y' ? "checked" : "") + '/> <label> Yes </label><input disabled type="radio"' + (gradingSegmentDetails[i].statusFlagSegmentA == 'N' ? "checked" : "") + '/> <label> No </label>' + '</td><tr>');
                } else if (gradingSegmentDetails[i].statusFlagSegmentC != null && gradingSegmentDetails[i].statusFlagSegmentC == 'Y') {
                    $('.fvrSegC').append('<tr><td>' + (i + 1) + '</td><td>' + (gradingSegmentDetails[i].category ? gradingSegmentDetails[i].category : '') + '</td><td><input disabled type="checkbox"' + (gradingSegmentDetails[i].statusFlagSegmentC == 'Y' ? "checked" : "") + '/> </td><tr>');
                } else {
                    $('.fvrSegB').append('<tr><td>' + (i + 1) + '</td><td>' + (gradingSegmentDetails[i].category ? gradingSegmentDetails[i].category : '') + '</td><td><input disabled type="radio"' + (gradingSegmentDetails[i].statusFlag == 'Y' ? "checked" : "") + '/> <label> Yes </label><input disabled type="radio"' + (gradingSegmentDetails[i].statusFlag == 'N' ? "checked" : "") + '/> <label> No </label>' + '</td><tr>');
                }
            }
        }
        document.getElementsByClassName('fvr-grading-modal')[0].style.display = "block";
    });

    // View FVR Document Details
    jQuery(document).on("click", '.fvrViewDocBtn', function() {
        if ($(this).closest('tr').attr('data-json')) {
            var docDetails = JSON.parse($(this).closest('tr').attr('data-json'));
            formFVRDocumentModalData(docDetails);
        }
    });

    $('#viewRejectionLetter').click(function() {
        var fileUrl = $(this).attr('data-fileUrl');
        if (fileUrl) {
            $('#frame').attr("src", fileUrl);
            document.getElementsByClassName('fvrDocumentViewModal')[0].style.display = "block";
            $('.fvrDocumentViewModal').addClass('dialogPosition');
        }
    });

    function getPreAuthDetails(response) {
        if (response) {
            $('#preauth-created-date').html(response.createdDate);
            $('#preauth-modified-date').html(response.modifiedDate);
            $('#preauth-remarks').html(response.remarks);
            $('#preauth-medical-remarks').html(response.medicalRemarks);
            $('#preauth-approved-amt').html(response.approvedAmt);
            $('#preauth-status').html(response.statusValue);
            $('#preauth-doctor-note').html(response.doctorNote);
            $('#preauth-doa').html(response.dateOfAdmisssion);
            $('#preauth-admission-reason').html(response.admissionReason);
            $('#preauth-change-doa').html(response.changeDOA);
            $('#preauth-room-cat').html(response.roomCategory);
            $('preauth-specialist-opinion').html(response.specialistOpinionTaken);
            $('#preauth-treatment-type').html(response.treatmentType);
            $('#preauth-specify-illness').html(response.specifyIllness);
            $('#preauth-days').html(response.noOfDays);
            $('#preauth-nature').html(response.natureOfTreatment);
            $('#preauth-critical-illness').html((response.criticalIllness == 'Y' ? '<input disabled type="checkbox" checked />Critical Illness' :
                '<input disabled type="checkbox"  />Critical Illness'));
            $('#preauth-corp-buffer').html((response.corpBuffer == 'Y' ? '<input disabled type="checkbox" checked />Corp Buffer' :
                '<input disabled type="checkbox"  />Corp Buffer'));
            $('#preauth-cons-date').html(response.firstConsultedDate);
            $('#preauth-specify-illness').html(response.specifyIllness);
            $('#preauth-auto-rest').html(response.autoRestore);
            $('#preauth-patient-status').html(response.patientStatus);
            $('#preauth-illness').html(response.illness);
            $('#preauth-death-date').html(response.dateOfDeath);
            $('#preauth-death-reason').html(response.deathReason);
            $('#preauth-relapse-remarks').html(response.relapseRemarks);
            $('#preauth-terminate-cover').html(response.terminateCover);
            $('#preauth-specialist-option').html((response.specialistOpinionTaken == '1' ? '<input type="radio" disabled name="branchfld" value="YES" checked />YES <input type="radio" name="branchfld" value="NO" disabled />NO' : '<input type="radio" name="branchfld" value="YES" disabled  />YES <input type="radio" name="branchfld" value="NO" disabled checked />NO'));

            $('#preauth-field-request').html((response.initiateFvr == '1' ? '<input type="radio" name="branch" value="YES" checked disabled/>YES <input type="radio" name="branch" value="NO"  disabled />NO' : '<input type="radio" name="branch" value="YES" disabled />YES <input type="radio" name="branch" value="NO" checked disabled />NO'));
            $('#preauth-fvr').html(response.fvrNotRequiredRemarks);
            $('#preauth-spl-type').html(response.specialistType);
            $('#preauth-allocation').html(response.allocationTo);
            $('#preauth-spl-consulted').html(response.specialistConsulted);
            $('#preauth-fvr-trigger').html(response.fvrTriggeredPoints);
            $('#preauth-spl-remarks').html(response.specialistRemarks);
            $('#preauth-inv-name').html(response.investigatorName);
            $('#preauth-treatment-remarks').html(response.treatmentRemarks);



            //Sub limits, Package & SI Restriction
            $('.preauth-sublimit-table-body').html('');
            var diagnosis = response.diagnosisTableList;
            for (var i = 0; i < diagnosis.length; i++) {
                $('.preauth-sublimit-table-body').append('<tr><td>' + (diagnosis[i].diagOrProcedure ? diagnosis[i].diagOrProcedure : '') + '</td><td>' + (diagnosis[i].description ? diagnosis[i].description : '') + '</td><td>' + (diagnosis[i].pedOrExclusion ? diagnosis[i].pedOrExclusion : '') + '</td><td>' + (diagnosis[i].isAmbChargeFlag == 'Y' ? '<input disabled type="checkbox" checked />' : '<input disabled type="checkbox" />') + '</td><td>' + (diagnosis[i].ambulanceCharge ? diagnosis[i].ambulanceCharge : '0') + '</td><td>' + (diagnosis[i].amountConsidered ? diagnosis[i].amountConsidered : '0') + '</td><td>' + (diagnosis[i].packageAmt ? diagnosis[i].packageAmt : 'NA') + '</td><td>' + (diagnosis[i].minimumAmountOfAmtconsideredAndPackAmt ? diagnosis[i].minimumAmountOfAmtconsideredAndPackAmt : '0') + '</td><td>' + (diagnosis[i].coPayPercentage ? diagnosis[i].coPayPercentage : '0') + '</td><td>' + (diagnosis[i].coPayAmount ? diagnosis[i].coPayAmount : '0') + '</td><td>' + (diagnosis[i].netAmount ? diagnosis[i].netAmount : '0') + '</td><td>' + (diagnosis[i].amtWithAmbulanceCharge ? diagnosis[i].amtWithAmbulanceCharge : '0') + '</td><td>' + (diagnosis[i].subLimitAmount ? diagnosis[i].subLimitAmount : 'NA') + '</td><td>' + (diagnosis[i].subLimitUtilAmount ? diagnosis[i].subLimitUtilAmount : '0') + '</td><td>' + (diagnosis[i].subLimitAvaliableAmt ? diagnosis[i].subLimitAvaliableAmt : '0') + '</td><td>' + (diagnosis[i].restrictionSI ? diagnosis[i].restrictionSI : 'NA') + '</td><td>' + (diagnosis[i].utilizedAmt ? diagnosis[i].utilizedAmt : '0') + '</td><td>' + (diagnosis[i].availableAmout ? diagnosis[i].availableAmout : '0') + '</td><td>' + (diagnosis[i].minimumAmount ? diagnosis[i].minimumAmount : '0') + '</td><td>' + (diagnosis[i].reverseAllocatedAmt ? diagnosis[i].reverseAllocatedAmt : '0') + '</td> </tr>');
            }


            $('.preauth-sublimit-table-body').append('<tr><td></td><td></td><td><b>Total</b></td><td></td><td></td><td><b>' + (response.totalDiagAllocAmt ? response.totalDiagAllocAmt : '') + '</b></td><td></td><td></td><td></td><td></td> <td><b>' + (response.totalDiagNetAmt ? response.totalDiagNetAmt : response.totalDiagPayableAmt) + '</b></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td><b>' + (response.totalDiagPayableAmt ? response.totalDiagPayableAmt : '') +
                '</b></td> <td><b>' + (response.totalDiagReverseAllocAmt ? response.totalDiagReverseAllocAmt : '') + '</b></td> </tr>');

            $('.preauth-amt-table-body').html('');
            $.each(response.amountConsideredValues, function(key, value) {
                if (key == 0) {
                    $('.preauth-amt-table-body').append('<tr><td>' + value + '</td><td>' + response.minimumValue + '</td></tr>');
                }

                if (key == 1) {
                    $('.preauth-amt-table-body').append('<tr><td>' + value + '</td><td>' + 0 + '</td></tr>');
                }

                if (key == 2) {
                    $('.preauth-amt-table-body').append('<tr><td>' + value + '</td><td>' + response.minimumValue + '</td></tr>');
                }
            });

            $('.preauth-balance-body').html('');
            $.each(response.balanceSumInsuredValues, function(key, value) {
                if (key == 0) {
                    $('.preauth-balance-body').append('<tr><td>' + value + '</td><td>' + response.balanceSumInsured + '</td></tr>');
                }

                if (key == 1) {
                    $('.preauth-balance-body').append('<tr><td>' + value + '</td><td>' + 0 + '</td></tr>');
                }

                if (key == 2) {
                    $('.preauth-balance-body').append('<tr><td>' + value + '</td><td>' + response.balanceSumInsured + '</td></tr>');
                }
            });

            $('#preauth-amount').val(response.approvedAmt);
            $('.preauth-amt-sublimit').val(response.totalDiagReverseAllocAmt);

            $('.preauth-restrict-table-body').html('');
            var claim = response.claimedDetailsList;
            for (var i = 0; i < claim.length; i++) {
                $('.preauth-restrict-table-body').append('<tr><td>' + (claim[i].description ? claim[i].description : '') + '</td><td>' + (claim[i].totalBillingDays ? claim[i].totalBillingDays : '') + '</td><td>' + (claim[i].billingPerDayAmount ? claim[i].billingPerDayAmount : '') + '</td><td>' + (claim[i].claimedBillAmount ? claim[i].claimedBillAmount : '') + '</td><td>' + (claim[i].deductibleAmount ? claim[i].deductibleAmount : '') + '</td><td>' + (claim[i].netAmount ? claim[i].netAmount : '0') + '</td><td>' + (claim[i].totalBillingDays ? claim[i].totalBillingDays : '') + '</td><td>' + (claim[i].policyPerDayPayment ? claim[i].policyPerDayPayment : '') + '</td><td>' + (claim[i].netAmount ? claim[i].netAmount : '0') + '</td><td>' + (claim[i].considerPerDayAmt ? claim[i].considerPerDayAmt : '') + '</td><td>' + (claim[i].nonPayableAmount ? claim[i].nonPayableAmount : '0') + '</td><td>' + (claim[i].paybleAmount ? claim[i].paybleAmount : '0') + '</td><td>' + (claim[i].nonPayableReason ? claim[i].nonPayableReason : '') + '</td> </tr>');

            }

            $('.preauth-restrict-table-body').append('<tr><td><b>Total</b></td><td></td><td></td><td><b>' + (response.totalClaimedAmtforAmtconsd ? response.totalClaimedAmtforAmtconsd : '0') + '</b></td><td><b>' + (response.totalDeductAmtforAmtconsd ? response.totalDeductAmtforAmtconsd : '0') + '</b></td><td><b>' + (response.totalNetAmtforAmtconsd ? response.totalNetAmtforAmtconsd : '0') + '</b></td><td></td><td><b>' + (response.totalProductAmtforAmtconsd ? response.totalProductAmtforAmtconsd : '0') + '</b></td><td></td><td></td><td></td><td><b>' + (response.totalPayableAmtforAmtconsd ? response.totalPayableAmtforAmtconsd : '') + '</b></td><td></td></tr>');

            $('.ped-validation-tbody').html('');

            var validation = response.pedValidationList;
            for (var i = 0; i < validation.length; i++) {
                $('.ped-validation-tbody').append('<tr><td>' + (validation[i].diagnosis ? validation[i].diagnosis : '') + '</td><td>' + (validation[i].pedName ? validation[i].pedName : '') + '</td><td>' + (validation[i].icdChapterValue ? validation[i].icdChapterValue : '') + '</td><td>' + (validation[i].icdBlockValue ? validation[i].icdBlockValue : '') + '</td><td>' + (validation[i].icdCodeValue ? validation[i].icdCodeValue : '') + '</td><td>' + (validation[i].policyAgeing ? validation[i].policyAgeing : '') + '</td><td>' + (validation[i].impactOnDiagnosis ? validation[i].impactOnDiagnosis : '') + '</td><td>' + (validation[i].consideredForExclusion ? validation[i].consideredForExclusion : '') + '</td><td>' + (validation[i].remarks ? validation[i].remarks : '') + '</td> </tr>');
            }

            $('.ped-exclusion-tbody').html('');
            var exclusion = response.procedureExclusionCheckTableList;
            for (var i = 0; i < exclusion.length; i++) {
                $('.ped-exclusion-tbody').append('<tr><td>' + (exclusion[i].procedureNameValue ? exclusion[i].procedureNameValue : '') + '</td><td>' + (exclusion[i].procedureCodeValue ? exclusion[i].procedureCodeValue : '') + '</td><td>' + (exclusion[i].packageAmount ? exclusion[i].packageAmount : '') + +'</td><td>' + (exclusion[i].policyAging ? exclusion[i].policyAging : '') + +'</td><td>' + (exclusion[i].procedureStatus ? exclusion[i].procedureStatus : '') + '</td><td>' + (exclusion[i].exclusionDetails ? exclusion[i].exclusionDetails : '') + '</td><td>' + (exclusion[i].remarks ? exclusion[i].remarks : '') + '</td> </tr>');
            }
            $('.overlay').hide();

        }
    }

    function getBillSummaryDetails() {
        if (authToken) {
            $.get('crcportal', {
                token: authToken,
                pageName: "bill_summary"
            }, function(data, statusText, xhr) {
                if (xhr.status == 200) {
                    $('.pre-hospitalization_tbody').html('');
                    $('.post-hospitalization_tbody').html('');
                    $('.hospitalization_tbody').html('');
                    if (data) {
                        if (data.isPASummaryDetails == false) {
                            $('#propDeduction').val((data.proportionateDeduction ? (data.proportionateDeduction == 'Y' ? "Yes" : "No") : ''));
                            $('#propDeductionAge').val((data.deductionPercentage ? data.deductionPercentage : '0.0'));

                            if (data.hospDetails) {

                                if (data.hospDetails.hospitalizationTabSummaryList) {
                                    var hospDetail = data.hospDetails.hospitalizationTabSummaryList;
                                    for (var i = 0; i < hospDetail.length; i++) {
                                        $('.hospitalization_tbody').append('<tr><td>' + (hospDetail[i].itemNoForView ? hospDetail[i].itemNoForView : '') + '</td><td>' + (hospDetail[i].itemName ? hospDetail[i].itemName : '') + '</td><td>' + (hospDetail[i].noOfDays ? hospDetail[i].noOfDays : '') + '</td><td>' + (hospDetail[i].perDayAmt ? hospDetail[i].perDayAmt : '') + '</td><td>' + (hospDetail[i].itemValue ? addCommas(hospDetail[i].itemValue) : '') + '</td><td>' + (hospDetail[i].noOfDaysAllowed ? hospDetail[i].noOfDaysAllowed : '') + '</td><td>' + (hospDetail[i].perDayAmtProductBased ? hospDetail[i].perDayAmtProductBased : '') + '</td><td>' + (hospDetail[i].amountAllowableAmount ? addCommas(hospDetail[i].amountAllowableAmount) : '') + '</td><td>' + (hospDetail[i].nonPayableProductBased ? hospDetail[i].nonPayableProductBased : '0') + '</td><td>' + (hospDetail[i].nonPayable ? hospDetail[i].nonPayable : '') + '</td><td>' + (hospDetail[i].proportionateDeduction ? hospDetail[i].proportionateDeduction : '') + '</td><td>' + (hospDetail[i].reasonableDeduction ? hospDetail[i].reasonableDeduction : '') + '</td><td>' + (hospDetail[i].totalDisallowances ? hospDetail[i].totalDisallowances : '') + '</td><td>' + (hospDetail[i].netPayableAmount ? hospDetail[i].netPayableAmount : '') + '</td><td>' + (hospDetail[i].deductibleOrNonPayableReason ? hospDetail[i].deductibleOrNonPayableReason : '') + '</td><td>' + (hospDetail[i].billingRemarks ? hospDetail[i].billingRemarks : '') + '</td><td></td></tr>');
                                    }

                                    $("table#hosp_table tr:even").css("background-color", "#c2cbce");
                                    $('.hospitalization_tbody').append('<tr class="subtotal"><td></td><td> Total </td><td></td><td></td><td>' + (data.hospDetails.totalClaimedAmt ? data.hospDetails.totalClaimedAmt : '0.0') + '</td><td></td><td></td><td>' + (data.hospDetails.amountTotal ? data.hospDetails.amountTotal : '0.0') + '</td><td>' + (data.hospDetails.nonpayableProdTotal ? data.hospDetails.nonpayableProdTotal : '0.0') + '</td><td>' + (data.hospDetails.nonpayableTotal ? data.hospDetails.nonpayableTotal : '0.0') + '</td><td>' + (data.hospDetails.propDecutTotal ? data.hospDetails.propDecutTotal : '0') + '</td><td>' + (data.hospDetails.reasonableDeducTotal ? data.hospDetails.reasonableDeducTotal : '0') + '</td><td>' + (data.hospDetails.disallowanceTotal ? data.hospDetails.disallowanceTotal : '0') + '</td><td>' + (data.hospDetails.totalApprovedAmt ? data.hospDetails.totalApprovedAmt : '0') + '</td><td></td><td></td><td></td></tr>');
                                }

                                if (data.hospDetails.preHospitalizationTabSummaryList) {
                                    var preHosp = data.hospDetails.preHospitalizationTabSummaryList;
                                    for (var i = 0; i < preHosp.length; i++) {
                                        $('.pre-hospitalization_tbody').append('<tr><td>' + (preHosp[i].sno ? preHosp[i].sno : '') + '</td><td>' + (preHosp[i].details ? preHosp[i].details : '') + '</td><td>' + (preHosp[i].claimedAmt ? preHosp[i].claimedAmt : '') + '</td><td>' + (preHosp[i].billingNonPayable ? preHosp[i].billingNonPayable : '') + '</td><td>' + (preHosp[i].reasonableDeduction ? preHosp[i].reasonableDeduction : '') + '</td><td>' + (preHosp[i].netAmount ? preHosp[i].netAmount : '') + '</td><td>' + (preHosp[i].reason ? preHosp[i].reason : '') + '</td><td>' + (preHosp[i].reason ? preHosp[i].reason : '') + '</td><td>' + (preHosp[i].reason ? preHosp[i].reason : '') + '</td></tr>');
                                    }
                                    $("table#prehosp_table tr:even").css("background-color", "#c2cbce");
                                    $('.pre-hospitalization_tbody').append('<tr class="subtotal"><td></td><td> Total Amount </td><td>' + (data.preclaimedAmt ? data.preclaimedAmt : '0') + '</td><td>' + (data.preNonPayableAmt ? data.preNonPayableAmt : '0') + '</td><td>' + (data.preReasonableDeduction ? data.preReasonableDeduction : '0') + '</td><td>' + (data.prePayableAmt ? data.prePayableAmt : '0') + '</td><td></td><td></td><td></td></tr>');
                                }

                                if (data.hospDetails.preHospitalizationTabSummaryList) {
                                    var postHosp = data.hospDetails.postHospitalizationTabSummaryList;

                                    for (var i = 0; i < postHosp.length; i++) {
                                        $('.post-hospitalization_tbody').append('<tr><td>' + (postHosp[i].sno ? postHosp[i].sno : '') + '</td><td>' + (postHosp[i].details ? postHosp[i].details : '') + '</td><td>' + (postHosp[i].claimedAmt ? postHosp[i].claimedAmt : '') + '</td><td>' + (postHosp[i].billingNonPayable ? postHosp[i].billingNonPayable : '') + '</td><td>' + (postHosp[i].reasonableDeduction ? postHosp[i].reasonableDeduction : '') + '</td><td>' + (postHosp[i].netAmount ? postHosp[i].netAmount : '') + '</td><td>' + (postHosp[i].reason ? postHosp[i].reason : '') + '</td><td>' + (preHosp[i].reason ? preHosp[i].reason : '') + '</td><td>' + (preHosp[i].reason ? preHosp[i].reason : '') + '</td></tr>');
                                    }
                                    $("table#posthosp_table tr:even").css("background-color", "#c2cbce");
                                    $('.post-hospitalization_tbody').append('<tr class="subtotal"><td></td><td> Total Amount </td><td>' + (data.postclaimedAmt ? data.postclaimedAmt : '0') + '</td><td>' + (data.postNonPayableAmt ? data.postNonPayableAmt : '0') + '</td><td>' + (data.postReasonableDeduction ? data.postReasonableDeduction : '0') + '</td><td>' + (data.postPayableAmt ? data.postPayableAmt : '0') + '</td><td></td><td></td><td></td></td></tr>');
                                }
                            }
                            if (data.billDetails) {
                                $('.viewBillDetails').attr('data-json', JSON.stringify(data.billDetails));
                                $('.viewBillDetails').attr('data-claimAmt', data.billDetailClaimedAmt);
                                $('.viewBillDetails').attr('data-amt', data.billDetailAmt);
                                $('.viewBillDetails').attr('data-nonpayable', data.billDetailNonPayableAmt);
                                $('.viewBillDetails').attr('data-payable', data.billDetailPayableAmt);
                            }
                            $('.PA_billdetails').addClass('hidden');
                            $('.billdetails').removeClass('hidden');
                        } else {
                            $('.benefits_tbody').html('');
                            $('.optional_covers_tbody').html('');
                            $('.additional_benefits_tbody').html('');
                            if (data.benefits) {
                                var benefits = data.benefits;
                                for (var i = 0; i < benefits.length; i++) {
                                    $('.benefits_tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (benefits[i].rodNo ? benefits[i].rodNo : '') + '</td><td>' + (benefits[i].fileType ? benefits[i].fileType : '') + '</td><td>' + (benefits[i].fileName ? benefits[i].fileName : '') + '</td><td>' + (benefits[i].classification ? benefits[i].classification.value : '') + '</td><td>' + (benefits[i].duration ? benefits[i].duration : '') + '</td><td>' + (benefits[i].billNo ? benefits[i].billNo : '') + '</td><td>' + (benefits[i].billDate ? benefits[i].billDate : '') + '</td><td>' + (benefits[i].billAmount ? benefits[i].billAmount : '') + '</td><td>' + (benefits[i].deduction ? benefits[i].deduction : '') + '</td><td>' + (benefits[i].netAmount ? benefits[i].netAmount : '0') + '</td><td>' + (benefits[i].eligibleAmount ? benefits[i].eligibleAmount : '') + '</td><td>' + (benefits[i].amtConsidered ? benefits[i].amtConsidered : '0') + '</td><td>' + (benefits[i].reasonForDeduction ? benefits[i].reasonForDeduction : '') + '</td></tr>');
                                }
                                $('.benefits_tbody').append('<tr class="subtotal"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>Total</td><td>' + data.totalBillAmt + '</td><td>' + data.totalDeduction + '</td><td>' + data.totalNetAmt + '</td><td>' + data.totalEligibleAmt + '</td><td>' + data.totalAmtConsidered + '</td><td></td></tr>');
                            }
                            if (data.optionalCovers) {
                                var optCover = data.optionalCovers;
                                for (var i = 0; i < optCover.length; i++) {
                                    $('.optional_covers_tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (optCover[i].optionalCover ? optCover[i].optionalCover.value : '') + '</td><td>' + (optCover[i].billNo ? optCover[i].billNo : '') + '</td><td>' + (optCover[i].billDate ? optCover[i].billDate : '') + '</td><td>' + (optCover[i].noOfDaysClaimed ? optCover[i].noOfDaysClaimed : '0') + '</td><td>' + (optCover[i].amountClaimedPerDay ? optCover[i].amountClaimedPerDay : '') + '</td><td>' + (optCover[i].totalClaimed ? optCover[i].totalClaimed : '') + '</td><td>' + (optCover[i].amtOfClaimPaid ? optCover[i].amtOfClaimPaid : '0') + '</td><td>' + (optCover[i].applicableSI ? optCover[i].applicableSI : '0') + '</td><td>' + (optCover[i].noOfDaysAllowed ? optCover[i].noOfDaysAllowed : '0') + '</td><td>' + (optCover[i].maxNoOfDaysPerHospital ? optCover[i].maxNoOfDaysPerHospital : '0') + '</td><td>' + (optCover[i].maxDaysAllowed ? optCover[i].maxDaysAllowed : '0') + '</td><td>' + (optCover[i].noOfDaysUtilised ? optCover[i].noOfDaysUtilised : '0') + '</td><td>' + (optCover[i].noOfDaysAvailable ? optCover[i].noOfDaysAvailable : '0') + '</td><td>' + (optCover[i].noOfDaysPayable ? optCover[i].noOfDaysPayable : '0') + '</td><td>' + (optCover[i].amtPerDayPayable ? optCover[i].amtPerDayPayable : '') + '</td><td>' + (optCover[i].allowedAmountPerDay ? optCover[i].allowedAmountPerDay : '0') + '</td><td>' + (optCover[i].siLimit ? optCover[i].siLimit : '') + '</td><td>' + (optCover[i].limit ? optCover[i].limit : '') +
                                        '</td><td>' + (optCover[i].balanceSI ? optCover[i].balanceSI : '') + '</td><td>' + (optCover[i].appAmt ? optCover[i].appAmt : '') + '</td></tr>');
                                }
                            }

                            if (data.additionalBenefits) {
                                var addBenefits = data.additionalBenefits;
                                for (var i = 0; i < addBenefits.length; i++) {
                                    $('.additional_benefits_tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (addBenefits[i].addonCovers ? addBenefits[i].addonCovers.value : '') + '</td><td>' + (addBenefits[i].noOfchildAgeLess18 ? addBenefits[i].noOfchildAgeLess18 : '') + '</td><td>' + (addBenefits[i].allowableChildren ? addBenefits[i].allowableChildren : '') + '</td><td>' + (addBenefits[i].billNo ? addBenefits[i].billNo : '') + '</td><td>' + (addBenefits[i].billDate ? addBenefits[i].billDate : '') + '</td><td>' + (addBenefits[i].billAmount ? addBenefits[i].billAmount : '') + '</td><td>' + (addBenefits[i].deduction ? addBenefits[i].deduction : '') + '</td><td>' + (addBenefits[i].netamount ? addBenefits[i].netamount : '0') + '</td><td>' + (addBenefits[i].siAddOnCover ? addBenefits[i].siAddOnCover : '') + '</td><td>' + (addBenefits[i].siLimit ? addBenefits[i].siLimit : '') + '</td><td>' + (addBenefits[i].eligibleAmount ? addBenefits[i].eligibleAmount : '') + '</td><td>' + (addBenefits[i].availableSI ? addBenefits[i].availableSI : '') + '</td><td>' + (addBenefits[i].approvedAmount ? addBenefits[i].approvedAmount : '') + '</td><td>' + (addBenefits[i].reasonForDeduction ? addBenefits[i].reasonForDeduction : '') + '</td><td>' + (addBenefits[i].whenPayable ? addBenefits[i].whenPayable : '') + '</td></tr>');
                                }
                            }

                            $('.PA_billdetails').removeClass('hidden');
                            $('.billdetails').addClass('hidden');
                        }


                    }
                    $('#bill-summary-dialog').dialog('open');
                } else if (xhr.status == 204) {
                    alert("Details not found");
                } else if (xhr.status == 400) {
                    alert("Bad Request");
                }
            });
        }
    }

    function getAcknowledgementDetails(data) {
        var showReconsiderTable = false;
        if (data.documentValues) {
            $('#ackNo').html((data.documentValues.acknowledgementNumber ? data.documentValues.acknowledgementNumber : ''));
            $('#ackCreatedById').html((data.documentValues.acknowledgmentCreatedId ? data.documentValues.acknowledgmentCreatedId : ''));
            $('#ackCreatedOn').html((data.documentValues.acknowledgmentCreateOn ? data.documentValues.acknowledgmentCreateOn : ''));
            $('#ackCreatedByName').html((data.documentValues.acknowledgmentCreatedName ? data.documentValues.acknowledgmentCreatedName : ''));
            $('#ackRcvdFrom').html((data.documentValues.documentReceivedFromValue ? data.documentValues.documentReceivedFromValue : ''));
            $('#ackEmailId').html((data.documentValues.emailId ? data.documentValues.emailId : ''));
            $('#ackRcvdDate').html((data.documentValues.documentReceivedDate ? data.documentValues.documentReceivedDate : ''));
            $('#ackContact').html((data.documentValues.acknowledgmentContactNumber ? data.documentValues.acknowledgmentContactNumber : ''));
            $('#ackRecipt').html((data.documentValues.modeOfReceiptValue ? data.documentValues.modeOfReceiptValue : ''));
            $('#ackReconsider').html((data.documentValues.reconsiderationRequestValue ? data.documentValues.reconsiderationRequestValue : ''));

            if (data.documentValues.reconsiderationRequestValue == "Yes") {
                showReconsiderTable = true;
            }

            $('.billRow1').html((data.documentValues.hospitalizationFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Hospitalization</td>' : '<td><input  type="checkbox" disabled />Hospitalization</td>') + (data.documentValues.preHospitalizationFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Pre-Hospitalization</td>' : '<td><input  type="checkbox" disabled />Pre-Hospitalization</td>') + (data.documentValues.postHospitalizationFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Post-Hospitalization</td>' : '<td><input  type="checkbox" disabled />Post-Hospitalization</td>') + (data.documentValues.partialHospitalizationFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Partial Hospitalization</td>' : '<td><input  type="checkbox" disabled />Partial Hospitalization</td>') + (data.documentValues.hospitalizationRepeat == 'Y' ? '<td><input  type="checkbox" checked disabled />Hospitalisation(Repeat)</td>' : '<td><input  type="checkbox" disabled />Hospitalisation(Repeat)</td>'));

            $('.billRow2').html((data.documentValues.lumpSumAmountFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Lumpsum Amount</td>' : '<td><input  type="checkbox" disabled />Lumpsum Amount</td>') + (data.documentValues.hospitalExpensesCoverFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Add on Benefits (Hospital cash)</td>' : '<td><input  type="checkbox" disabled />Add on Benefits (Hospital cash)</td>') + (data.documentValues.hospitalExpensesCoverFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Add on Benefits (Patient Care)</td>' : '<td><input  type="checkbox" disabled />Add on Benefits (Patient Care)</td>') + (data.documentValues.hospitalizationRepeatFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Hospitalisation(Repeat)</td>' : '<td><input  type="checkbox" disabled />Hospitalisation(Repeat)</td>'));

            $('.billRow3').html((data.documentValues.emergencyMedicalEvaluationFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Emergency Medical Evacuation</td>' : '<td><input  type="checkbox" disabled />Emergency Medical Evacuation</td>') + (data.documentValues.compassionateTravelFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Compassionate Travel</td>' : '<td><input  type="checkbox" disabled />Compassionate Travel</td>') + (data.documentValues.repatriationOfMortalRemainsFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Repatriation Of Mortal Remains</td>' : '<td><input  type="checkbox" disabled />Repatriation Of Mortal Remains</td>'));

            $('.billRow4').html((data.documentValues.preferredNetworkHospitalFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Preferred Network Hospital</td>' : '<td><input  type="checkbox" disabled />Preferred Network Hospital</td>') + (data.documentValues.sharedAccomodationFlag == 'Y' ? '<td><input  type="checkbox" checked disabled />Shared Accomodation</td>' : '<td><input  type="checkbox" disabled />Shared Accomodation</td>'));
        }

        if (data.ackCheckList) {
            $('.ackChecklistBody').html('');
            var checklist = data.ackCheckList;
            for (var i = 0; i < checklist.length; i++) {
                $('.ackChecklistBody').append('<tr><td>' + (i + 1) + '</td><td style="width: 30%">' + (checklist[i].value ? checklist[i].value : '') + '</td><td>' + (checklist[i].receivedStatus ? checklist[i].receivedStatus.value : '') + '</td><td>' + (checklist[i].noOfDocuments ? checklist[i].noOfDocuments : '') + '</td><td>' + (checklist[i].remarks ? checklist[i].remarks : '') + '</td></tr>');
            }
        }

        if (data.ackQueryList) {
            $('.ackQueryDetailsBody').html('');
            var queryList = data.ackQueryList;
            for (var i = 0; i < queryList.length; i++) {
                $('.ackQueryDetailsBody').append('<tr id="ack_' + (i + 1) + '" data-key="" data-dms=""><td>' + (i + 1) + '</td><td style="width: 30%">' + (queryList[i].rodNo ? queryList[i].rodNo : '') + '</td><td>' + (queryList[i].billClassification ? queryList[i].billClassification : '') + '</td><td>' + (queryList[i].diagnosis ? queryList[i].diagnosis : '') + '</td><td>' + (queryList[i].claimedAmount ? queryList[i].claimedAmount : '') + '</td><td>' + (queryList[i].queryRaisedRole ? queryList[i].queryRaisedRole : '') + '</td><td>' + (queryList[i].queryRaisedDateString ? queryList[i].queryRaisedDateString : '') +
                    '</td><td>' + (queryList[i].queryStatus ? queryList[i].queryStatus : '') + '</td><td>' + (queryList[i].queryReplyStatus ? queryList[i].queryReplyStatus : '') + '</td><td><a class="ackDetailBtn hreflink" >View Details </a></td></tr>');
                $("#ack_" + (i + 1)).attr('data-key', JSON.stringify(queryList[i].queryDetails));
                $("#ack_" + (i + 1)).attr('data-dms', JSON.stringify(queryList[i].dmsDocumentDto));
            }
        }

        if (showReconsiderTable == true && data.ackReconsiderRODList) {
            $('.ackReconsiderROD').removeClass('hidden');
            $('.ackRODBody').html('');
            var rodList = data.ackReconsiderRODList;
            for (var i = 0; i < rodList.length; i++) {
                $('.ackRODBody').append('<tr><td>' + (rodList[i].rodNo ? rodList[i].rodNo : '') + '</td><td>' + (rodList[i].billClassification ? rodList[i].billClassification : '') + '</td><td>' + (rodList[i].claimedAmt ? rodList[i].claimedAmt : '') + '</td><td>' + (rodList[i].approvedAmt ? rodList[i].approvedAmt : '') + '</td><td>' + (rodList[i].rodStatus ? rodList[i].rodStatus : '') + '</td><td></td></tr>');
            }
        } else {
            $('.ackReconsiderROD').addClass('hidden');
        }


    }

    // Get the modal
    var modal = document.getElementById('myModal');

    // Get the button that opens the modal
    //var btn = document.getElementById("myBtn");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    // When the user clicks the button, open the modal 
    /* btn.onclick = function() {
    	modal.style.display = "block";
    } */

    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
        modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    function pop1() {

        window.open('', 'rejectionDetails', 'width:650, height: 500');
    }

    function pop2() {

        window.open('', 'queryDetails', 'width:650, height: 500');
    }

    /* Formatting function for row details */
    function portabilityTableFormat(portDetails) {

        var detailViewHtml = '<tr>' +
            '<td><strong>TBA code</strong></td>' +
            '<td >' + (portDetails.tbaCode ? portDetails.tbaCode : '') + '</td>' +
            '<td><strong>1st SI</strong></td>' +
            '<td>' + (portDetails.siFist ? portDetails.siFist : '0') + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td><strong>Policy Start</strong></td>' +
            '<td>' + (portDetails.policyStartDate ? portDetails.policyStartDate : '') + '</td>' +
            '<td><strong>11nd SI</strong></td>' +
            '<td>' + (portDetails.siSecond ? portDetails.siSecond : '0') + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td><strong>Period elapsed</strong></td>' +
            '<td>' + (portDetails.periodElapsed ? portDetails.periodElapsed : '0') + '</td>' +
            '<td><strong>3rd SI</strong></td>' +
            '<td>' + (portDetails.siThird ? portDetails.siThird : '0') + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td><strong>policy Term</strong></td>' +
            '<td>' + (portDetails.policyTerm ? portDetails.policyTerm : '') + '</td>' +
            '<td><strong>4th SI</strong></td>' +
            '<td>' + (portDetails.siFourth ? portDetails.siFourth : '0') + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td><strong>Date of Birth</strong></td>' +
            '<td>' + (portDetails.dateOfBirth ? portDetails.dateOfBirth : '') + '</td>' +
            '<td><strong>I st  Float SI</strong></td>' +
            '<td>' + (portDetails.siFirstFloat ? portDetails.siFirstFloat : '0') + '</td>' +
            '</tr> ' +
            '<tr><td></td><td></td>' +
            '<td><strong>II nd  float SI</strong></td>' +
            '<td>' + (portDetails.siSecondFloat ? portDetails.siSecondFloat : '0') + '</td>' +
            '</tr> <tr></tr> ' +
            '<tr>' +
            '<td><strong>3rd Float SI</strong></td>' +
            '<td>' + (portDetails.siThirdFloat ? portDetails.siThirdFloat : '0') + '</td>' +
            '<td><strong>Remarks</strong></td>' +
            '<td>' + (portDetails.remarks ? portDetails.remarks : '') + '</td>' +
            '</tr> ' +
            '<tr>' +
            '<td><strong>4th  Float SI</strong></td>' +
            '<td>' + (portDetails.siFourthFloat ? portDetails.siFourthFloat : '0') + '</td>' +
            '<td><strong>Ped Declared</strong></td>' +
            '<td>' + (portDetails.pedDeclared ? portDetails.pedDeclared : '') + '</td>' +
            '</tr> ' +
            '<tr>' +
            '<td><strong>I st Change SI</strong></td>' +
            '<td>' + (portDetails.siFirstChange ? portDetails.siFirstChange : '0') + '</td>' +
            '<td><strong>PED ICD code</strong></td>' +
            '<td>' + (portDetails.pedIcdCode ? portDetails.pedIcdCode : '') + '</td>' +
            '</tr> ' +
            '<tr>' +
            '<td><strong>II nd Change SI</strong></td>' +
            '<td>' + (portDetails.siSecondChange ? portDetails.siSecondChange : '0') + '</td>' +
            '<td><strong>PED Description</strong></td>' +
            '<td>' + (portDetails.pedDescription ? portDetails.pedDescription : '') + '</td>' +
            '</tr> ' +
            '<tr>' +
            '<td><strong>3rd Change SI</strong></td>' +
            '<td>' + (portDetails.siThirdChange ? portDetails.siThirdChange : '0') + '</td>' +
            '<td><strong>Family size</strong></td>' +
            '<td>' + (portDetails.familySize ? portDetails.familySize : '0') + '</td>' +
            '</tr> ' +
            '<tr>' +
            '<td><strong>4th Change SI</strong></td>' +
            '<td>' + (portDetails.siFourthChange ? portDetails.siFourthChange : '0') + '</td>' +
            '<td><strong>Request ID</strong></td>' +
            '<td>' + (portDetails.requestId ? portDetails.requestId : '') + '</td>' +
            '</tr>' +
            '<tr>' +
            '<td></td>' +
            '<td ></td>' +
            '<td><strong>Member entry dt.</strong></td>' +
            '<td>' + (portDetails.memberEntryDate ? portDetails.memberEntryDate : '') + '</td>' +
            '</tr> ';

        return detailViewHtml;

    }

    function sumInsuredTableFormat(jsonDetails, tableName) {

        var json = jsonDetails;
        var section;
        var insuredTableBody = '';
        for (var i = 0; i < json.length; i++) {
            if (tableName == "sectionI") {
                section = json[i].sectionI;
            } else if (tableName == "sectionII") {
                section = json[i].sectionII;
            } else if (tableName == "sectionIII") {
                section = json[i].sectionIII;
            } else if (tableName == "sectionIV") {
                section = json[i].sectionIV;
            } else if (tableName == "sectionV") {
                section = json[i].sectionV;
            } else if (tableName == "sectionVI") {
                section = json[i].sectionV;
            } else if (tableName == "sectionVIII") {
                section = json[i].sectionIII;
            }

            insuredTableBody += '<tr><td>' + (section ? section : '') + '</td><td>' + (json[i].cover ? json[i].cover : '') + '</td><td>' + (json[i].subCover ? json[i].subCover : '') + '</td><td>' + (tableName == "sectionI" ? addCommas(json[i].sumInsured) : addCommas(json[i].limit)) + '</td><td>' + (json[i].claimPaid ? addCommas(json[i].claimPaid) : '0') + '</td><td>' + (json[i].claimOutStanding ? addCommas(json[i].claimOutStanding) : '0') + '</td><td>' + (json[i].balance ? addCommas(json[i].balance) : '0') + '</td><td>' + (json[i].provisionCurrentClaim ? addCommas(json[i].provisionCurrentClaim) : '0') + '</td><td>' + (json[i].balanceSI ? addCommas(json[i].balanceSI) : '0') + '</td><tr>';
        }

        var formattedTables = '<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">' +
            '<thead>' +
            '<tr>' +
            '<th>Section</th>' +
            '<th>Cover</th>' +
            '<th>Sub Cover</th>' + (tableName == "sectionI" ? '<th>Sum Insured</th>' : '<th>limit</th>') +
            '<th>Claim Paid</th>' +
            '<th>Claim Outstanding</th>' +
            '<th>Balance</th>' +
            '<th>Provision for Current Claim</th>' +
            '<th>Balance Sum Insured after Provision</th>' +
            '</tr>' +
            '</thead>' +
            '<tbody class="' + tableName + '">' + insuredTableBody + '</tbody>' +
            '</table>';

        return formattedTables;
    }

    $("#download").click(function() {

        var pdf = new jsPDF("l", "mm", "a4");
        var imgData = canvas.toDataURL('image/jpeg', 1.0);
        var fileName = $(this).attr('data-filename');

        pdf.addImage(imgData, 'JPEG', 10, 10, 180, 150);
        pdf.save(fileName);
    });

    $("#print").click(function() {
        var win = window.open();
        win.document.write("<br><img src='" + canvas.toDataURL() + "'/>");
        win.print();
        win.location.reload();
    });

    $("#zoomIn").click(function() {
        /*if (scale == 3) {
        	return;
        } else {
        	$('canvas').remove();
        	scale = scale + 0.25;
        	loadPdf(pdfData);
        }*/
        canvas.style.width = '100% !important';
    });

    $("#zoomOut").click(function() {
        if (scale == 2) {
            return;
        } else {
            $('canvas').remove();
            scale = scale - 0.25;
            loadPdf(pdfData);
        }
    });

    jQuery(document).on("click", '.prev_clm_preauth', function() {
        $('.inner-overlay').show();
        $('.overlay').show();
        var preauthKey = $(this).attr('data-id');
        var preauthValue = $(this).attr('data-value');
        var datalink = $(this).attr('data-link');
        if (preauthKey && authToken) {
            $.get('crcportal', {
                token: authToken,
                pageName: "pre_auth_details",
                preIntimationKey: preauthKey
            }, function(data, statusText, xhr) {
                if (xhr.status == 200) {
                    getPreAuthDetails(data.preAuthDetails);
                    $('.preselect').html('');
                    $(".preselect").append('<option value="' + preauthKey + '">' + preauthValue + '</option>');
                    $('#preauth-details-dialog').dialog('open');
                    if (datalink == "subPage") {
                        $('.previous_claim_status_dialog').css('display', 'none');
                        $('#preauth-details-dialog').parent('div').find('.ui-dialog-titlebar-close').addClass('prevAuthCloseBtn');
                    }
                } else if (xhr.status == 204) {
                    alert("Details not found");
                } else if (xhr.status == 400) {
                    alert("Bad Request");
                }
                $('.inner-overlay').hide();
                $('.overlay').hide();
            });
        }
    });

    jQuery(document).on("click", '.prevClmDocAckDetailClk', function() {
        $('.inner-overlay').show();
        $('.overlay').show();
        var datalink = $(this).attr('data-link');
        var ackKey = $(this).attr('data-ackKey');
        if (authToken && ackKey) {
            $.get('crcportal', {
                token: authToken,
                pageName: "previous_acknowledgment_details",
                ackKey: ackKey
            }, function(data, statusText, xhr) {
                if (xhr.status == 200) {
                    if (data) {
                        getAcknowledgementDetails(data);
                        if (datalink == "subPage") {
                            $('.previous_claim_status_dialog').css('display', 'none');
                        }
                        $('#acknowledge-details-dialog').parent('div').find('.ui-dialog-titlebar-close').addClass('prevAuthCloseBtn prevAckClose');
                        document.getElementsByClassName('prevAcknowledgementDialog')[0].style.display = "none";
                        $('#acknowledge-details-dialog').dialog('open');
                    }
                } else if (xhr.status == 204) {
                    alert("Details not found");
                } else if (xhr.status == 400) {
                    alert("Bad Request");
                }
            });
        }
        $('.inner-overlay').hide();
        $('.overlay').hide();
        return false;
    });

    jQuery(document).on("click", '.prevBillClick', function() {
        $('.inner-overlay').show();
        $('.overlay').show();
        var datalink = $(this).attr('data-link');
        getBillSummaryDetails();
        if (datalink == "subPage") {
            $('.previous_claim_status_dialog').css('display', 'none');
            $('#bill-summary-dialog').parent('div').find('.ui-dialog-titlebar-close').addClass('prevAuthCloseBtn');
        }
        $('.inner-overlay').hide();
        $('.overlay').hide();
    });

    jQuery(document).on("click", '.prevBillDetailClick', function() {
        $('.inner-overlay').show();
        $('.overlay').show();
        var rodKey = $(this).attr('data-rodKey');
        var datalink = $(this).attr('data-link');
        if (authToken && rodKey) {
            $.get('crcportal', {
                token: authToken,
                pageName: "bill_details",
                rodKey: rodKey
            }, function(data, statusText, xhr) {
                if (xhr.status == 200) {
                    if (data) {
                        var billDetails = data.billDetails;
                        $('.viewBillDetails').attr('data-json', JSON.stringify(data.billDetails));
                        var netClaimAmount = data.billDetailClaimedAmt;
                        var netAmount = data.billDetailAmt;
                        var netNonpayableAmount = data.billDetailNonPayableAmt;
                        var netPayableAmount = data.billDetailPayableAmt;

                        if (billDetails) {
                            $('.bill-details-tbody').html('');
                            for (var i = 0; i < billDetails.length; i++) {
                                $('.bill-details-tbody').append('<tr><td>' + +(i + 1) + '</td><td>' + (billDetails[i].rodNumber ? billDetails[i].rodNumber : '') + '</td><td>' + (billDetails[i].fileType ? billDetails[i].fileType : '') + '</td><td>' + (billDetails[i].fileName ? billDetails[i].fileName : '') + '</td><td>' + (billDetails[i].billNumber ? billDetails[i].billNumber : '') + '</td><td>' + (billDetails[i].billDate ? billDetails[i].billDate : '') + '</td><td>' + (billDetails[i].noOfItems ? billDetails[i].noOfItems : '') + '</td><td>' + (billDetails[i].billValue ? billDetails[i].billValue : '') + '</td><td>' + (billDetails[i].itemNo ? billDetails[i].itemNo : '') + '</td><td>' + (billDetails[i].itemName ? billDetails[i].itemName : '') + '</td><td>' + (billDetails[i].classification ? billDetails[i].classification : '') + '</td><td>' + (billDetails[i].category ? billDetails[i].category : '') + '</td><td>' + (billDetails[i].noOfDays ? billDetails[i].noOfDays : '') + '</td><td>' + (billDetails[i].perDayAmt ? billDetails[i].perDayAmt : '') + '</td><td>' + (billDetails[i].claimedAmount ? billDetails[i].claimedAmount : '') + '</td><td>' + (billDetails[i].entitlementNoOfDays ? billDetails[i].entitlementNoOfDays : '') + '</td><td>' + (billDetails[i].entitlementPerDayAmt ? billDetails[i].entitlementPerDayAmt : '') + '</td><td>' + (billDetails[i].amount ? billDetails[i].amount : '') + '</td><td>' + (billDetails[i].deductionNonPayableAmount ? billDetails[i].deductionNonPayableAmount : '') + '</td><td>' + (billDetails[i].payableAmount ? billDetails[i].payableAmount : '') + '</td><td>' + (billDetails[i].reason ? billDetails[i].reason : '') + '</td><td>' + (billDetails[i].medicalRemarks ? billDetails[i].medicalRemarks : '') + '</td><td>' + (billDetails[i].irdaLevel1 ? billDetails[i].irdaLevel1 : '') + '</td><td>' + (billDetails[i].irdaLevel2 ? billDetails[i].irdaLevel2 : '') + '</td><td>' + (billDetails[i].irdaLevel3 ? billDetails[i].irdaLevel3 : '') + '</td></tr>');
                            }
                            $("table.billdetail_table tr:even").css("background-color", "#c2cbce");
                            $('.bill-details-tbody').append('<tr class="subtotal"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>' + (netClaimAmount ? netClaimAmount : '') + '</td><td></td><td></td><td>' + (netAmount ? netAmount : '') + '</td><td>' + (netNonpayableAmount ? netNonpayableAmount : '') + '</td><td>' + (netPayableAmount ? netPayableAmount : '') + '</td><td></td><td></td><td></td><td></td><td></td></tr>');
                            document.getElementsByClassName('bill-details-modal')[0].style.display = "block";
                            if (datalink == "subPage") {
                                $('.previous_claim_status_dialog').css('display', 'none');
                                $('.billDetailsCloseIcon').addClass('prevBillClicked');
                            }
                        }
                    }
                } else if (xhr.status == 204) {
                    alert("Details not found");
                } else if (xhr.status == 400) {
                    alert("Bad Request");
                }
                $('.inner-overlay').hide();
                $('.overlay').hide();
            });
        }
    });


});

jQuery(document).on("click", '.prevAuthCloseBtn', function() {

    if ($(this).hasClass('prevAckClose')) {
        document.getElementsByClassName('prevAcknowledgementDialog')[0].style.display = "block";
        $(this).removeClass('prevAckClose');
    } else {
        $('.previous_claim_status_dialog').css('display', 'block');
    }
    $(this).removeClass('prevAuthCloseBtn');
});

jQuery(document).on("click", '.fvrViewDocumentDetailBtn', function() {
    var fileName = $(this).attr('data-fileName');
    var fileToken = $(this).attr('data-fileToken');

    if (fileName && fileToken) {
        $.get('crcportal', {
            token: authToken,
            pageName: "fetch_dms_document",
            fileName: fileName,
            fileToken: fileToken
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                if (data) {
                    $('#frame').attr("src", data.documentUrl);
                    document.getElementsByClassName('fvrDocumentViewModal')[0].style.display = "block";
                }
            } else if (xhr.status == 204) {
                alert("Details not found");
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
        });
    }
});

jQuery(document).on("click", '.ackDetailBtn', function() {
    var ackDetail = JSON.parse($(this).closest('tr').attr('data-key'));
    if (ackDetail) {
        $('#ackQryIntNo').html((ackDetail.intimationNo ? ackDetail.intimationNo : ''));
        $('#ackQryclaimNo').html((ackDetail.claimNo ? ackDetail.claimNo : ''));
        $('#ackQryPolicyNo').html((ackDetail.policyNo ? ackDetail.policyNo : ''));
        $('#ackQryAckNo').html((ackDetail.acknowledgementNo ? ackDetail.acknowledgementNo : ''));
        $('#ackQryRODNo').html((ackDetail.rodNumber ? ackDetail.rodNumber : ''));
        $('#ackQryRcvdFrom').html((ackDetail.receivedFrom ? ackDetail.receivedFrom : ''));
        $('#ackQryBill').html((ackDetail.billClassification ? ackDetail.billClassification : ''));
        $('#ackQryProductName').html((ackDetail.productName ? ackDetail.productName : ''));
        $('#ackQryClaimType').html((ackDetail.claimType ? ackDetail.claimType : ''));
        $('#ackQryInsuredName').html((ackDetail.insuredPatientName ? ackDetail.insuredPatientName : ''));
        $('#ackQryHospName').html((ackDetail.hospitalName ? ackDetail.hospitalName : ''));
        $('#ackQryHospCity').html((ackDetail.hospitalCity ? ackDetail.hospitalCity : ''));
        $('#ackQryHospType').html((ackDetail.hospitalType ? ackDetail.hospitalType : ''));
        $('#ackQryDOA').html((ackDetail.admissionDate ? ackDetail.admissionDate : ''));
        $('#ackQryDiagnosis').html((ackDetail.diagnosis ? ackDetail.diagnosis : ''));
        $('#ackQryRaisedRole').html((ackDetail.queryRaiseRole ? ackDetail.queryRaiseRole : ''));
        $('#ackQryRaisedId').html((ackDetail.queryRaised ? ackDetail.queryRaised : ''));
        $('#ackQryRaisedDate').html((ackDetail.queryRaisedDateStr ? ackDetail.queryRaisedDateStr : ''));
        $('#ackQryRemarks').html((ackDetail.queryRemarks ? ackDetail.queryRemarks : ''));
        $('#ackQryDraftDate').html((ackDetail.queryDraftedDate ? ackDetail.queryDraftedDate : '-'));
        $('#ackQryLetterRemarks').html((ackDetail.queryLetterRemarks ? ackDetail.queryLetterRemarks : ''));
        $('#ackQryDate').html((ackDetail.approvedRejectedDate ? ackDetail.approvedRejectedDate : ''));
        $('#ackQryRejRemarks').html((ackDetail.rejectedRemarks ? ackDetail.rejectedRemarks : ''));
        $('#ackQryRedraftRemarks').html((ackDetail.redraftRemarks ? ackDetail.redraftRemarks : ''));
        $('#ackQryStatus').html((ackDetail.queryStatus ? ackDetail.queryStatus : ''));
        var ackQryLetter = $(this).closest('tr').attr('data-dms');
        if (ackQryLetter) {
            $('#ackViewQryLetter').attr('data-dms', ackQryLetter);
        }
        $("table.ackQryDetailsTable tr:odd").css("background-color", "#c5e0dc");
        document.getElementsByClassName('ackQryDetailsModal')[0].style.display = "block";
        if ($(this).hasClass('viaPrevQryclk')) {
            $('.ackQryDetailsModal').addClass('dialogPosition');
            $('#ackViewQryLetter').addClass('viaPrevQryclk');
            $(this).removeClass('viaPrevQryclk');
        }
    }
});

jQuery(document).on("click", '.viewFileBtn', function() {
    var docDetail = JSON.parse($(this).closest('tr').attr('data-docs'));
    var optionName = $(this).closest('tr').attr('data-name');
    $('.coorViewFileBody').html('');
    if (docDetail) {
        for (var i = 0; i < docDetail.length; i++) {
            $('.coorViewFileBody').append('<tr><td>' + (i + 1) + '</td><td>' + docDetail[i].fileName + '</td><td>' +
                (docDetail[i].fileUrl ? '<a data-name="' + optionName + '"' + (docDetail[i].fileUrl.indexOf("docx") == -1 ? 'class="coorViewDocumentDetailBtn"' : 'href="' + docDetail[i].fileUrl + '" target="_blank"') + ' data-url="' + docDetail[i].fileUrl : '') + '">View Document </a></td></tr>');
        }
        document.getElementsByClassName('coorViewFileModal')[0].style.display = "block";
    }
});

jQuery(document).on("click", '.coorViewDocumentDetailBtn', function() {
    var docUrl = $(this).attr('data-url');
    $('#frame').attr("src", docUrl);
    $('#frame').parent('div').parent('div').find('.fvrDocumentViewCloseIcon, .fvrDocumentViewCloseBtn').addClass('temp ' + $(this).attr('data-name'));
    $('.coorViewFileModal').addClass('hidden');
    if ($(this).attr('data-name') == "coordinator") {
        $('.coordinator-reply-dialog').addClass('hidden');
    } else {
        $('.specialist-trail-dialog').addClass('hidden');
    }
    document.getElementsByClassName('fvrDocumentViewModal')[0].style.display = "block";
});

jQuery(document).on("click", '.prevInsDetails', function() {
    var insDetail = JSON.parse($(this).closest('tr').attr('data-json'));
    if (insDetail) {
        var optionName = $(this).closest('tr').attr('data-name');
        $('.insuredDetailBody').html('');
        if (insDetail) {
            for (var i = 0; i < insDetail.length; i++) {
                $('.insuredDetailBody').append('<tr><td>' + (i + 1) + '</td><td>' + (insDetail[i].insuredName ? insDetail[i].insuredName : '') + '</td><td>' + (insDetail[i].sex ? insDetail[i].sex : '') + '</td><td>' + (insDetail[i].dateOfBirth ? insDetail[i].dateOfBirth : '') + '</td><td>' + (insDetail[i].age ? insDetail[i].age : '') + '</td><td>' + (insDetail[i].relation ? insDetail[i].relation : '') + '</td><td>' + (insDetail[i].sumInsured ? insDetail[i].sumInsured : '0') + '</td> <td>' + (insDetail[i].pedDescription ? insDetail[i].pedDescription : '') + '</td><tr>');
            }
            document.getElementsByClassName('insuredDetailModal')[0].style.display = "block";
        }
    } else {
        alert("Insured Details not found")
    }
});

jQuery(document).on("click", '.prevPolicyView', function() {
    var url = $(this).attr('data-url');
    if (url) {
        window.open(url, "popUp", "resizable=yes");
    }
});

jQuery(document).on("click", '.prevClaimDoctorNotesCloseIcon', function() {
    document.getElementsByClassName('prevClaimDoctorNotesDialog')[0].style.display = "none";
    $('.prevClaimDoctorNotesDialog').removeClass('dialogPosition');
});

jQuery(document).on("click", '.prevClaimTrailsCloseIcon', function() {
    document.getElementsByClassName('prevClaimTrailsDialog')[0].style.display = "none";
    if ($(this).hasClass('temp')) {
        $('.previous_claim_status_dialog').css('display', 'block');
        $(this).removeClass('temp');
    }
});

jQuery(document).on("click", '.prevClaimPedModalCloseIcon', function() {
    document.getElementsByClassName('prevClaimPedModal')[0].style.display = "none";
});

jQuery(document).on("click", '.prevClaimStatusModalCloseIcon', function() {
    document.getElementsByClassName('previous_claim_status_dialog')[0].style.display = "none";
});

jQuery(document).on("click", '.closureCloseIcon', function() {
    document.getElementsByClassName('closure-detail-modal')[0].style.display = "none";
    $('.previous_claim_status_dialog').css('display', 'block');
});

jQuery(document).on("click", '.paymentDetailCloseIcon', function() {
    document.getElementsByClassName('prev_claim_payment_modal')[0].style.display = "none";
    if ($(this).hasClass('temp')) {
        $('.previous_claim_status_dialog').css('display', 'block');
        $(this).removeClass('temp');
    }
});

jQuery(document).on("click", '.prevClaimQueryCloseIcon', function() {
    document.getElementsByClassName('prev_claim_query_modal')[0].style.display = "none";
    if ($(this).hasClass('temp')) {
        $('.previous_claim_status_dialog').css('display', 'block');
        $(this).removeClass('temp');
    }
});

jQuery(document).on("click", '.prevPedClk', function() {
    $('.inner-overlay').show();
    $('.overlay').show();
    var claimKey = $(this).closest('tr').attr('data-claimKey').trim();
    if (authToken && claimKey) {
        $.get('crcportal', {
            token: authToken,
            pageName: "previous_claim_details_ped",
            claimKey: claimKey
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                $('.prevClaimPedBody').html('');
                if (data.prevClaimPedDetails) {
                    var pedDetail = data.prevClaimPedDetails;
                    if (pedDetail.length > 0) {
                        for (var i = 0; i < pedDetail.length; i++) {
                            $('.prevClaimPedBody').append('<tr id="prevclm_ped_' + (i + 1) + '" data-json="" data-ped=""><td>' + (i + 1) + '</td><td>' + (pedDetail[i].intimationNo ? pedDetail[i].intimationNo : '') + '</td><td>' + (pedDetail[i].pedSuggestionName ? pedDetail[i].pedSuggestionName : '') + '</td><td>' + (pedDetail[i].nameOfPed ? pedDetail[i].nameOfPed : '') + '</td><td>' + (pedDetail[i].repLetterDate ? pedDetail[i].repLetterDate : '') + '</td><td>' + (pedDetail[i].remarks ? pedDetail[i].remarks : '') + '</td><td>' + (pedDetail[i].requestedId ? pedDetail[i].requestedId : '') + '</td><td>' + (pedDetail[i].requestedDate ? pedDetail[i].requestedDate : '') + '</td><td>' + (pedDetail[i].requestStatus ? pedDetail[i].requestStatus : '') + '</td><td><a class="hreflink prevPedDetailsBtn">View Details</a></td><td><a href="" class="hreflink prevPedHistoryBtn">View Trails</a></td></tr>');
                            $("#prevclm_ped_" + (i + 1)).attr('data-json', JSON.stringify(pedDetail[i].pedHistoryList));
                            $("#prevclm_ped_" + (i + 1)).attr('data-ped', JSON.stringify(pedDetail[i]));
                        }
                    }
                    document.getElementsByClassName('prevClaimPedModal')[0].style.display = "block";
                } else if (data.prevPedPremiaUrl) {
                    window.open(data.prevPedPremiaUrl, 'PED Details', 'View PED Details');
                }
            } else if (xhr.status == 204) {
                alert("Details not found");
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
        });
    }
    $('.inner-overlay').hide();
    $('.overlay').hide();
    return false;
});

jQuery(document).on("click", '.prevPedHistoryBtn', function() {
    var pedHistory = JSON.parse($(this).closest('tr').attr('data-json'));
    $('.pedHistoryBody').html('');
    for (var i = 0; i < pedHistory.length; i++) {
        $('.pedHistoryBody').append('<tr><td>' + (pedHistory[i].status ? pedHistory[i].status : '') + '</td><td>' + (pedHistory[i].strDateAndTime ? pedHistory[i].strDateAndTime : '') + '</td><td style="width: 10%">' + (pedHistory[i].userName ? pedHistory[i].userName : '') + '</td><td>' + (pedHistory[i].remarks ? pedHistory[i].remarks : '') + '</td></tr>');
    }
    document.getElementsByClassName('pedHistoryModal')[0].style.display = "block";
    $('.pedHistoryModal').addClass('dialogPosition');
    return false;
});

jQuery(document).on("click", '.investigationHistoryBtn', function() {
    var investHistory = JSON.parse($(this).closest('tr').attr('data-json'));
    setInvestigationHistoryDetails(investHistory);
    return false;
});

jQuery(document).on("click", '.investigationDetailHistoryBtn', function() {
    var investHistory = JSON.parse($(this).attr('data-json'));
    setInvestigationHistoryDetails(investHistory);
    return false;
});

function setInvestigationHistoryDetails(investHistory) {
    $('.investHistoryBody').html('');
    if (investHistory) {
        for (var i = 0; i < investHistory.length; i++) {
            $('.investHistoryBody').append('<tr><td>' + (i + 1) + '</td><td>' + (investHistory[i].reassignDateValue ? investHistory[i].reassignDateValue : '') + '</td><td>' + (investHistory[i].createdBy ? investHistory[i].createdBy : '') + '</td><td style="width: 10%">' + (investHistory[i].createdByName ? investHistory[i].createdByName : '') + '</td><td>' + (investHistory[i].assignedFrom ? investHistory[i].assignedFrom : '') + '</td><td>' + (investHistory[i].investigatorName ? investHistory[i].investigatorName : '') + '</td><td style="width: 10%">' + (investHistory[i].invTelNo ? investHistory[i].invTelNo : '') + '</td><td>' + (investHistory[i].invMobileNo ? investHistory[i].invMobileNo : '') + '</td><td>' + (investHistory[i].reAssignRemarks ? investHistory[i].reAssignRemarks : '') + '</td></tr>');
        }
    }
    document.getElementsByClassName('investHistoryModal')[0].style.display = "block";
    $('.investHistoryModal').addClass('dialogPosition');
}

jQuery(document).on("click", '.prevPedDetailsBtn', function() {
    var pedDetail = JSON.parse($(this).closest('tr').attr('data-ped'));
    $('.viewPedSug').html((pedDetail.pedSuggestionName ? pedDetail.pedSuggestionName : ''));
    $('.viewPedName').html((pedDetail.nameOfPed ? pedDetail.nameOfPed : ''));
    $('.viewRepdate').html((pedDetail.repLetterDate ? pedDetail.repLetterDate : ''));
    $('.viewPedRemarks').html((pedDetail.remarks ? pedDetail.remarks : ''));
    $('.pedDetailBody').html('');
    if (pedDetail.viewPedEndoresementDetails) {
        // View PED Endorsement Details
        var pedEndorseDetails = pedDetail.viewPedEndoresementDetails;
        for (var i = 0; i < pedEndorseDetails.length; i++) {
            $('.pedDetailBody').append('<tr><td>' + (pedEndorseDetails[i].pedCode ? pedEndorseDetails[i].pedCode : '') + '</td><td>' + (pedEndorseDetails[i].description ? pedEndorseDetails[i].description : '') + '</td><td style="width: 10%">' + (pedEndorseDetails[i].icdChapter ? pedEndorseDetails[i].icdChapter : '') + '</td><td>' + (pedEndorseDetails[i].icdBlock ? pedEndorseDetails[i].icdBlock : '') + '</td><td>' + (pedEndorseDetails[i].icdCode ? pedEndorseDetails[i].icdCode : '') + '</td><td>' + (pedEndorseDetails[i].source ? pedEndorseDetails[i].source : '') + '</td><td>' + (pedEndorseDetails[i].othersSpecify ? pedEndorseDetails[i].othersSpecify : '') + '</td><td>' + (pedEndorseDetails[i].doctorRemarks ? pedEndorseDetails[i].doctorRemarks : '') + '</td></tr>');
        }
    } else {
        $('.pedDetailBody').append('<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>');
    }

    // View PED Ammended Details
    if (pedDetail.ammendedEndorsement) {
        var pedAmmendedDetails = pedDetail.ammendedEndorsement;

        $('.viewPedSugAmd').val((pedAmmendedDetails.pedSuggestionName ? pedAmmendedDetails.pedSuggestionName : ''));
        $('.viewPedNameAmd').val((pedAmmendedDetails.pedName ? pedAmmendedDetails.pedName : ''));
        $('.viewRepdateAmd').val((pedDetail.repLetterDate ? pedDetail.repLetterDate : ''));
        $('.viewPedRemarksAmd').val((pedAmmendedDetails.remarks ? pedAmmendedDetails.remarks : ''));
        $('.viewReqIdAmd').val((pedAmmendedDetails.requestorId ? pedAmmendedDetails.requestorId : ''));
        $('.viewReqDateAmd').val((pedDetail.pedRequestorDate ? pedDetail.pedRequestorDate : ''));
        var endorseDetails = pedAmmendedDetails.viewPEDEndoresementDetailsDTO;
        $('.ped-ammended-table-body').html('');
        for (var i = 0; i < endorseDetails.length; i++) {
            $('.ped-ammended-table-body').append('<tr><td>' + (endorseDetails[i].description ? endorseDetails[i].description : '') + '</td><td>' + (endorseDetails[i].pedCode ? endorseDetails[i].pedCode : '') + '</td><td style="width: 10%">' + (endorseDetails[i].icdChapter ? endorseDetails[i].icdChapter : '') + '</td><td>' + (endorseDetails[i].icdBlock ? endorseDetails[i].icdBlock : '') + '</td><td>' + (endorseDetails[i].icdCode ? endorseDetails[i].icdCode : '') + '</td><td>' + (endorseDetails[i].source ? endorseDetails[i].source : '') + '</td><td>' + (endorseDetails[i].othersSpecify ? endorseDetails[i].othersSpecify : '') + '</td><td>' + (endorseDetails[i].doctorRemarks ? endorseDetails[i].doctorRemarks : '') + '</td></tr>');
        }
        $('.ped-ammended-div').removeClass('hidden');
    } else {
        $('.ped-ammended-div').addClass('hidden');
    }

    if (pedDetail.hasPedSpecialistDetails) {
        $('.viewPedStatusSpl').html((pedDetail.pedSpecialistStatus ? pedDetail.pedSpecialistStatus : ''));
        $('.viewPedRemarksSpl').html((pedDetail.pedSpecialistRemarks ? pedDetail.pedSpecialistRemarks : ''));
        $('.ped-specialist-div').removeClass('hidden');
        $('.ped-specialist-empty-div').addClass('hidden');
    } else {
        $('.ped-specialist-div').addClass('hidden');
        $('.ped-specialist-empty-div').removeClass('hidden');
    }


    if (pedDetail.pedDiscussedWith) {
        $('.discussedWith').show();
        $('.viewDiscussedWith').html(pedDetail.pedDiscussedWith);
    } else {
        $('.discussedWith').hide();
    }

    if (pedDetail.pedSuggestion) {
        $('.suggestion').show();
        $('.viewSuggestion').html(pedDetail.pedSuggestion);
    } else {
        $('.suggestion').hide();
    }

    $('.viewReqId').html((pedDetail.pedRequestorId ? pedDetail.pedRequestorId : ''));
    $('.viewReqDate').html((pedDetail.pedRequestorDate ? pedDetail.pedRequestorDate : ''));

    // View PED Approved Details
    if (pedDetail.approverEndorsement) {
        var pedApprovedDetails = pedDetail.approverEndorsement;

        $('.viewPedSugApp').val((pedApprovedDetails.pedSuggestionName ? pedApprovedDetails.pedSuggestionName : ''));
        $('.viewPedNameApp').val((pedApprovedDetails.pedName ? pedApprovedDetails.pedName : ''));
        $('.viewRepdateApp').val(repLetterDate);
        $('.viewPedRemarksApp').val((pedApprovedDetails.remarks ? pedApprovedDetails.remarks : ''));

        var endorseDetails = pedApprovedDetails.viewPEDEndoresementDetailsDTO;
        $('.ped-approved-table-body').html('');
        for (var i = 0; i < endorseDetails.length; i++) {
            $('.ped-approved-table-body').append('<tr><td>' + (endorseDetails[i].description ? endorseDetails[i].description : '') + '</td><td>' + (endorseDetails[i].pedCode ? endorseDetails[i].pedCode : '') + '</td><td style="width: 10%">' + (endorseDetails[i].icdChapter ? endorseDetails[i].icdChapter : '') + '</td><td>' + (endorseDetails[i].icdBlock ? endorseDetails[i].icdBlock : '') + '</td><td>' + (endorseDetails[i].icdCode ? endorseDetails[i].icdCode : '') + '</td><td>' + (endorseDetails[i].source ? endorseDetails[i].source : '') + '</td><td>' + (endorseDetails[i].othersSpecify ? endorseDetails[i].othersSpecify : '') + '</td><td>' + (endorseDetails[i].doctorRemarks ? endorseDetails[i].doctorRemarks : '') + '</td></tr>');
        }

        $('.viewPedStatusApp').val((pedApprovedDetails.pedSuggestionName ? pedApprovedDetails.pedSuggestionName : ''));
        $('.viewQueryApp').val((pedApprovedDetails.queryRemarks ? pedApprovedDetails.queryRemarks : ''));
        $('.viewSplApp').val((pedApprovedDetails.specialistType ? pedApprovedDetails.specialistType : ''));
        $('.viewReasonApp').val((pedApprovedDetails.reasonforReferring ? pedApprovedDetails.reasonforReferring : ''));
        $('.viewRejectionApp').val((pedApprovedDetails.rejectionRemarks ? pedApprovedDetails.rejectionRemarks : ''));
        $('.viewApprovalApp').val((pedApprovedDetails.approvalRemarks ? pedApprovedDetails.approvalRemarks : ''));


        $('.ped-approved-div').removeClass('hidden');
    } else {
        $('.ped-approved-div').addClass('hidden');
    }

    document.getElementsByClassName('ped-detail-modal')[0].style.display = "block";
    $('.ped-detail-modal').addClass('dialogPosition');
    return false;
});

jQuery(document).on("click", '.prevSplTrailsClk', function() {
    $('.inner-overlay').show();
    $('.overlay').show();
    if (authToken) {
        $.get('crcportal', {
            token: authToken,
            pageName: "specialist_trail"
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                $('.specialist-trail-body').html('');
                if (data.specialistOpinionDetails) {
                    var splDetail = data.specialistOpinionDetails;
                    for (var i = 0; i < splDetail.length; i++) {
                        $('.specialist-trail-body').append('<tr id="spl_' + (i + 1) + '" data-docs="" data-name="specialist"><td>' + (i + 1) + '</td><td>' + (splDetail[i].requestedDate ? splDetail[i].requestedDate : '') + '</td><td>' +
                            (splDetail[i].repliedDate ? splDetail[i].repliedDate : '') + '</td><td>' + (splDetail[i].specialistType ? splDetail[i].specialistType : '') + '</td><td>' + (splDetail[i].specialistDrNameId ? splDetail[i].specialistDrNameId : '') + '</td><td>' + (splDetail[i].requestorNameId ? splDetail[i].requestorNameId : '') + '</td><td>' +
                            (splDetail[i].requestorRemarks ? splDetail[i].requestorRemarks : '') + '</td><td><a class="viewFileBtn hreflink" > View File </a></td> <td>' + (splDetail[i].specialistRemarks ? splDetail[i].specialistRemarks : '') + '</td><tr>');
                        $("#spl_" + (i + 1)).attr('data-docs', JSON.stringify(splDetail[i].uploadedDocumentList));
                    }
                }
                $('#specialist-trail-dialog').dialog('open');
                $('#specialist-trail-dialog').addClass('dialogPosition');
            } else if (xhr.status == 204) {
                alert("Details not found");
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
        });
    }
    $('.inner-overlay').hide();
    $('.overlay').hide();
    return false;
});

jQuery(document).on("click", '.prevTrailsClk', function() {
    $('.inner-overlay').show();
    $('.overlay').show();
    var claimKey = $(this).closest('tr').attr('data-claimKey').trim();
    if (authToken && claimKey) {
        $.get('crcportal', {
            token: authToken,
            pageName: "previous_claim_history_details",
            claimKey: claimKey
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                $('.prevClaimTrails_tbody').html('');
                if (data.prevClaimHistoryDetails) {
                    var history = data.prevClaimHistoryDetails;
                    if (history.length > 0) {
                        for (var i = 0; i < history.length; i++) {
                            $('.prevClaimTrails_tbody').append('<tr><td>' + (history[i].typeofClaim ? history[i].typeofClaim : '') + '</td><td>' + (history[i].docrecdfrom ? history[i].docrecdfrom : '') + '</td><td>' + (history[i].billClassif ? history[i].billClassif : '') + '</td><td>' + (history[i].rodtype ? history[i].rodtype : '') + '</td><td>' + (history[i].referenceNo ? history[i].referenceNo : '') + '</td><td>' + (history[i].dateAndTime ? history[i].dateAndTime : '') + '</td><td>' + (history[i].userID ? history[i].userID : '') + '</td><td>' + (history[i].userName ? history[i].userName : '') + '</td><td>' + (history[i].claimStage ? history[i].claimStage : '') + '</td><td>' + (history[i].status ? history[i].status : '') + '</td><td>' + (history[i].userRemark ? history[i].userRemark : '') + '</td></tr>');
                        }
                    }
                    document.getElementsByClassName('prevClaimTrailsDialog')[0].style.display = "block";
                    $('.prevClaimTrailsDialog').addClass('dialogPosition');
                } else if (data.prevHistoryPremiaUrl) {
                    window.open(data.prevHistoryPremiaUrl, 'Previous Claim History Details', 'Previous Claim History Details');
                }
            } else if (xhr.status == 204) {
                alert("Details not found");
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
        });
    }
    $('.inner-overlay').hide();
    $('.overlay').hide();
    return false;
});

jQuery(document).on("click", '.prevClmTrailsClk', function() {
    $('.inner-overlay').show();
    $('.overlay').show();
    var rodKey = $(this).attr('data-rodKey');
    var datalink = $(this).attr('data-link');
    var intimationKey = $(this).attr('data-intKey');
    if (authToken && rodKey && intimationKey) {
        $.get('crcportal', {
            token: authToken,
            pageName: "previous_trails",
            rodKey: rodKey,
            intimationKey: intimationKey
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                $('.prevClaimTrails_tbody').html('');
                if (data.historyDetails) {
                    var history = data.historyDetails;
                    if (history.length > 0) {
                        for (var i = 0; i < history.length; i++) {
                            $('.prevClaimTrails_tbody').append('<tr><td>' + (history[i].typeofClaim ? history[i].typeofClaim : '') + '</td><td>' + (history[i].docrecdfrom ? history[i].docrecdfrom : '') + '</td><td>' + (history[i].billClassif ? history[i].billClassif : '') + '</td><td>' + (history[i].rodtype ? history[i].rodtype : '') + '</td><td>' + (history[i].referenceNo ? history[i].referenceNo : '') + '</td><td>' + (history[i].dateAndTime ? history[i].dateAndTime : '') + '</td><td>' + (history[i].userID ? history[i].userID : '') + '</td><td>' + (history[i].userName ? history[i].userName : '') + '</td><td>' + (history[i].claimStage ? history[i].claimStage : '') + '</td><td>' + (history[i].status ? history[i].status : '') + '</td><td>' + (history[i].userRemark ? history[i].userRemark : '') + '</td></tr>');
                        }
                    }
                    if (datalink == "subPage") {
                        $('.previous_claim_status_dialog').css('display', 'none');
                        $('.prevClaimTrailsCloseIcon').addClass('temp');
                    }
                    document.getElementsByClassName('prevClaimTrailsDialog')[0].style.display = "block";

                }
            } else if (xhr.status == 204) {
                alert("Details not found");
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
            $('.inner-overlay').hide();
            $('.overlay').hide();
        });
    }

    return false;
});

jQuery(document).on("click", '.prevClmDocAcknowledgementClk', function() {
    $('.inner-overlay').show();
    $('.overlay').show();
    var rodKey = $(this).attr('data-rodKey');
    var datalink = $(this).attr('data-link');
    if (authToken && rodKey) {
        $.get('crcportal', {
            token: authToken,
            pageName: "previous_acknowledgment_list",
            rodKey: rodKey
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                $('.prevAckDetails_tbody').html('');
                if (data.acknowledgementDetails) {
                    var ackDetail = data.acknowledgementDetails;
                    for (var i = 0; i < ackDetail.length; i++) {
                        $('.prevAckDetails_tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (ackDetail[i].acknowledgeNumber ? ackDetail[i].acknowledgeNumber : '') + '</td><td>' + (ackDetail[i].receivedFromValue ? ackDetail[i].receivedFromValue : '') + '</td><td>' + (ackDetail[i].strDocumentReceivedDate ? ackDetail[i].strDocumentReceivedDate : '') + '</td><td>' + (ackDetail[i].modeOfReceipt ? ackDetail[i].modeOfReceipt.value : '') + '</td><td>' + (ackDetail[i].billClassification ? ackDetail[i].billClassification : '') + '</td><td class="hreflink prevClmDocAckDetailClk" data-link="' + datalink + '" data-ackKey="' + (ackDetail[i].key ? ackDetail[i].key : '') + '" >View Acknowledgement</td></tr>');
                    }
                    document.getElementsByClassName('prevAcknowledgementDialog')[0].style.display = "block";
                    if (datalink == "subPage") {
                        $('.previous_claim_status_dialog').css('display', 'none');
                        $('.prevAckCloseIcon').addClass('temp');
                    }
                }
            } else if (xhr.status == 204) {
                alert("Details not found");
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
            $('.inner-overlay').hide();
            $('.overlay').hide();
        });
    }
    return false;
});

jQuery(document).on("click", '.rejLinkClick', function() {
    $('.inner-overlay').show();
    $('.overlay').show();
    var rodNo = $(this).attr('data-rodNo');
    var datalink = $(this).attr('data-link');
    if (authToken && rodNo) {
        $.get('crcportal', {
            token: authToken,
            pageName: "rejection_details",
            rodNo: rodNo
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                if (data.rejectionDetails) {
                    var rejDetail = data.rejectionDetails;
                    if (rejDetail != null) {
                        $('#rejIntNo').html((rejDetail.intimationNo ? rejDetail.intimationNo : ''));
                        $('#rejClaimNo').html((rejDetail.claimNo ? rejDetail.claimNo : ''));
                        $('#rejPolicyNo').html((rejDetail.policyNo ? rejDetail.policyNo : ''));
                        $('#rejClaimType').html((rejDetail.claimType ? rejDetail.claimType : ''));
                        $('#rejInsuredName').html((rejDetail.insuredPatientName ? rejDetail.insuredPatientName : ''));
                        $('#rejHospName').html((rejDetail.hospitalName ? rejDetail.hospitalName : ''));
                        $('#rejHospCity').html((rejDetail.hospitalCity ? rejDetail.hospitalCity : ''));
                        $('#rejHospType').html((rejDetail.hospitalType ? rejDetail.hospitalType : ''));
                        $('#rejDOA').html((rejDetail.admissionDate ? rejDetail.admissionDate : ''));
                        $('#rejDiagnosis').html((rejDetail.diagnosis ? rejDetail.diagnosis : ''));
                        $('#rejRaisedRole').html((rejDetail.rejectedRole ? rejDetail.rejectedRole : ''));
                        $('#rejRaisedId').html((rejDetail.rejectedName ? rejDetail.rejectedName : ''));
                        $('#rejRaisedDate').html((rejDetail.rejectedDate ? rejDetail.rejectedDate : ''));
                        $('#rejDraftDate').html((rejDetail.draftedDate ? rejDetail.draftedDate : ''));
                        $('#rejCategory').html((rejDetail.rejCategValue ? rejDetail.rejCategValue : ''));
                        $('#rejLetterRemarks').html((rejDetail.letterRectionRemarks ? rejDetail.letterRectionRemarks : ''));
                        $('#rejDate').html((rejDetail.approveRejectionDate ? rejDetail.approveRejectionDate : ''));
                        $('#rejRemarks').html((rejDetail.rejectionRemarks ? rejDetail.rejectionRemarks : ''));
                        $('#rejDisapprovedRemarks').html((rejDetail.disApprovalRemarks ? rejDetail.disApprovalRemarks : ''));
                        $('#rejRedraftRemarks').html((rejDetail.reDraftRemarks ? rejDetail.reDraftRemarks : ''));
                        $('#rejStatus').html((rejDetail.rejectionStatus ? rejDetail.rejectionStatus : ''));
                        if (rejDetail.rejLetterFileUrl) {
                            $('#viewRejectionLetter').attr('data-fileUrl', (rejDetail.rejLetterFileUrl ? rejDetail.rejLetterFileUrl : ''));
                            $('#viewRejectionLetter').removeClass('hidden');
                        } else {
                            $('#viewRejectionLetter').addClass('hidden');
                        }
                    }
                    document.getElementsByClassName('rejectionDetailsModal')[0].style.display = "block";
                    $("table.ackQryDetailsTable tr:odd").css("background-color", "#c5e0dc");
                    if (datalink == "subPage") {
                        $('.previous_claim_status_dialog').css('display', 'none');
                        $('.rejectionDetailCloseBtn').addClass('temp');
                    }
                }
            } else if (xhr.status == 204) {
                document.getElementsByClassName('rejectionDetailsModal')[0].style.display = "block";
                $("table.ackQryDetailsTable tr:odd").css("background-color", "#c5e0dc");
                if (datalink == "subPage") {
                    $('.previous_claim_status_dialog').css('display', 'none');
                    $('.rejectionDetailCloseBtn').addClass('temp');
                }
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
            $('.inner-overlay').hide();
            $('.overlay').hide();
        });
    }
    return false;
});

jQuery(document).on("click", '.prevDoctorViewClk', function() {
    $('.inner-overlay').show();
    $('.overlay').show();
    var claimKey = $(this).closest('tr').attr('data-claimKey').trim();
    if (authToken && claimKey) {
        $.get('crcportal', {
            token: authToken,
            pageName: "previous_claim_doctor_remarks",
            claimKey: claimKey
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                $('.prevClaimDoctorNotes_tbody').html('');
                if (data.prevClaimDoctorRemarks) {
                    var remarks = data.prevClaimDoctorRemarks;
                    if (remarks.length > 0) {
                        for (var i = 0; i < remarks.length; i++) {
                            $('.prevClaimDoctorNotes_tbody').append('<tr><td>' + (remarks[i].strNoteDate ? remarks[i].strNoteDate : '') + '</td><td>' + (remarks[i].userId ? remarks[i].userId : '') + '</td><td>' + (remarks[i].transaction ? remarks[i].transaction : '') + '</td><td>' + (remarks[i].transactionType ? remarks[i].transactionType : '') + '</td><td>' + (remarks[i].remarks ? remarks[i].remarks : '') + '</td></tr>');
                        }
                    }
                    document.getElementsByClassName('prevClaimDoctorNotesDialog')[0].style.display = "block";
                    $('.prevClaimDoctorNotesDialog').addClass('dialogPosition');
                } else if (data.prevDoctorPremiaUrl) {
                    window.open(data.prevDoctorPremiaUrl, 'Previous Claim History Details', 'Previous Claim History Details');
                }
            } else if (xhr.status == 204) {
                alert("Details not found");
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
            $('.inner-overlay').hide();
            $('.overlay').hide();
        });
    }
    return false;
});

jQuery(document).on("click", '.prev_payment_click', function() {
    $('.inner-overlay').show();
    $('.overlay').show();
    var rodKey = $(this).attr('data-rodNo');
    var claimType = $(this).attr('data-claimType');
    var datalink = $(this).attr('data-link');
    if (authToken && rodKey) {
        $.get('crcportal', {
            token: authToken,
            pageName: "payment_details",
            rodKey: rodKey
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                if (data.paymentTrails) {
                    var paymentTrails = data.paymentTrails;
                    if (paymentTrails) {
                        $('.payment-detail-tbody').html('');
                        for (var i = 0; i < paymentTrails.length; i++) {
                            $('.payment-detail-tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (claimType ? claimType : '') + '</td><td>' + (paymentTrails[i].dateTimeStr ? paymentTrails[i].dateTimeStr : '') + '</td><td>' + (paymentTrails[i].userId ? paymentTrails[i].userId : '') + '</td><td>' + (paymentTrails[i].claimStage ? paymentTrails[i].claimStage : '') + '</td><td>' + (paymentTrails[i].claimStatus ? paymentTrails[i].claimStatus : '') + '</td><td>' + (paymentTrails[i].paymentMode ? paymentTrails[i].paymentMode : '') + '</td><td>' + (paymentTrails[i].reasonForChange ? paymentTrails[i].reasonForChange : '') + '</td></tr>');
                        }
                    }
                }
                if (datalink == "subPage") {
                    $('.previous_claim_status_dialog').css('display', 'none');
                    $('.paymentDetailCloseIcon').addClass('temp');
                }
                document.getElementsByClassName('prev_claim_payment_modal')[0].style.display = "block";
            } else if (xhr.status == 204) {
                alert("Details not found");
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
            $('.inner-overlay').hide();
            $('.overlay').hide();
        });
    }
    return false;
});

jQuery(document).on("click", '.viewClosureDetails', function() {
    $('.inner-overlay').show();
    $('.overlay').show();
    var rodKey = $(this).attr('data-rodKey');
    var rodNo = $(this).attr('data-rodNo');
    var intimationNo = $(this).attr('data-intimationNo');
    if (authToken && rodKey) {
        $.get('crcportal', {
            token: authToken,
            pageName: "closure_details",
            rodKey: rodKey
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                $('#closureIntNo').html(intimationNo);
                $('#closureRodNo').html(rodNo);
                if (data.closureDetails) {
                    var closure = data.closureDetails;
                    $('.closure-tbody').html('');
                    for (var i = 0; i < closure.length; i++) {
                        $('.closure-tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (closure[i].closedDate ? closure[i].closedDate : '') + '</td><td>' + (closure[i].closedBy ? closure[i].closedBy : '') + '</td><td>' + (closure[i].reasonForClosure ? closure[i].reasonForClosure : '') + '</td></tr>');
                    }
                }
                document.getElementsByClassName('closure-detail-modal')[0].style.display = "block";
                $('.previous_claim_status_dialog').css('display', 'none');
            } else if (xhr.status == 204) {
                $('#closureIntNo').html(intimationNo);
                $('#closureRodNo').html(rodNo);
                document.getElementsByClassName('closure-detail-modal')[0].style.display = "block";
                $('.previous_claim_status_dialog').css('display', 'none');
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
            $('.inner-overlay').hide();
            $('.overlay').hide();
        });
    }
    return false;
});

jQuery(document).on("click", '.prevQueryClick', function() {
    $('.inner-overlay').show();
    $('.overlay').show();
    var rodKey = $(this).attr('data-rodKey');
    var claimKey = $(this).attr('data-claimKey');
    var datalink = $(this).attr('data-link');
    if (authToken && rodKey && claimKey) {
        $.get('crcportal', {
            token: authToken,
            pageName: "previous_query_details",
            rodKey: rodKey,
            claimKey: claimKey
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                if (data.queryDetails) {
                    $('.prev-claim-query-tbody').html('');
                    var queryList = data.queryDetails;
                    for (var i = 0; i < queryList.length; i++) {
                        $('.prev-claim-query-tbody').append('<tr id="prevQry_' + (i + 1) + '" data-key="" data-dms=""><td>' + (i + 1) + '</td><td style="width: 30%">' + (queryList[i].acknowledgementNo ? queryList[i].acknowledgementNo : '') + '</td><td>' + (queryList[i].rodNumber ? queryList[i].rodNumber : '') + '</td><td>' + (queryList[i].diagnosis ? queryList[i].diagnosis : '') + '</td><td>' + (queryList[i].queryRemarks ? queryList[i].queryRemarks : '') + '</td><td>' + (queryList[i].queryRaiseRole ? queryList[i].queryRaiseRole : '') + '</td><td>' + (queryList[i].designation ? queryList[i].designation : '') +
                            '</td><td>' + (queryList[i].queryRaisedDateStr ? queryList[i].queryRaisedDateStr : '') + '</td><td>' + (queryList[i].queryStatus ? queryList[i].queryStatus : '') + '</td><td><a class="ackDetailBtn hreflink viaPrevQryclk" >View Details </a></td></tr>');
                        $("#prevQry_" + (i + 1)).attr('data-key', JSON.stringify(queryList[i]));
                        $("#prevQry_" + (i + 1)).attr('data-dms', JSON.stringify(queryList[i].dmsDocumentDto));
                    }
                }
                if (datalink == "subPage") {
                    $('.previous_claim_status_dialog').css('display', 'none');
                    $('.prevClaimQueryCloseIcon').addClass('temp');
                }
                document.getElementsByClassName('prev_claim_query_modal')[0].style.display = "block";
            } else if (xhr.status == 204) {
                alert("Details not found");
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
            $('.inner-overlay').hide();
            $('.overlay').hide();
        });
    }
    return false;
});

jQuery(document).on("click", '.prevClaimStatusClk', function() {
    $('.inner-overlay').show();
    $('.overlay').show();
    var intimationNo = $(this).closest('tr').attr('data-intimationNo').trim();
    if (authToken && intimationNo) {
        $.get('crcportal', {
            token: authToken,
            pageName: "previous_claim_intimation",
            intimationNo: intimationNo
        }, function(data, statusText, xhr) {
            if (xhr.status == 200) {
                if (data.intimation) {
                    var intimation = data.intimation;
                    $('#prev_clm_intNo').html((intimation.intimationNo ? intimation.intimationNo : ""));
                    $('#prev_clm_intPolNo').html((intimation.policyNo ? intimation.policyNo : ""));
                    $('#prev_clm_intDate').html((intimation.dateAndTime ? intimation.dateAndTime : ""));
                    $('#prev_clm_intIssueOffice').html((intimation.issueOffice ? intimation.issueOffice : ""));
                    $('#prev_clm_intCpuCode').html((intimation.cpuCode ? intimation.cpuCode : ""));
                    $('#prev_clm_intProductName').html((intimation.productName ? intimation.productName : ""));
                    $('#prev_clm_intMode').html((intimation.intimationMode ? intimation.intimationMode : ""));
                    $('#prev_clm_intProposerName').html((intimation.proposerName ? intimation.proposerName : ""));
                    $('#prev_clm_intIntimatedBy').html((intimation.intimatedBy ? intimation.intimatedBy : ""));
                    $('#prev_clm_intState').html((intimation.state ? intimation.state : ""));
                    $('#prev_clm_intPatientName').html((intimation.patientName ? intimation.patientName : ""));
                    $('#prev_clm_intCity').html((intimation.city ? intimation.city : ""));
                    $('#prev_clm_intIsCovered').html((intimation.isPatientCovered == true ? '<input disabled type="checkbox" checked />Patient Not Covered' : '<input disabled type="checkbox"  />Patient Not Covered'));
                    $('#prev_clm_intArea').html((intimation.area ? intimation.area : ""));
                    $('#prev_clm_intHealthCardNo').html((intimation.healthCardNo ? intimation.healthCardNo : ""));
                    $('#prev_clm_intHospName').html((intimation.hospName ? intimation.hospName : ""));
                    $('#prev_clm_intName').html((intimation.name ? intimation.name : ""));
                    $('#prev_clm_intAddress').html((intimation.address ? intimation.address : ""));
                    $('#prev_clm_intRelation').html((intimation.relationship ? intimation.relationship : ""));
                    $('#prev_clm_intAdDate').html((intimation.admissionDate ? intimation.admissionDate : ""));
                    $('#prev_clm_intAdType').html((intimation.admissionType ? intimation.admissionType : ""));
                    $('#prev_clm_intInpatient').html((intimation.inpatient ? intimation.inpatient : ""));
                    $('#prev_clm_intHosptype').html((intimation.hospType ? intimation.hospType : ""));
                    $('#prev_clm_intLateIntimation').html((intimation.lateIntimation ? intimation.lateIntimation : ""));
                    $('#prev_clm_intHospCode').html((intimation.hospCode ? intimation.hospCode : ""));
                    $('#prev_clm_intAdReason').html((intimation.admissionReason ? intimation.admissionReason : ""));
                    $('#prev_clm_intHospCodeIrda').html((intimation.irdaHospCode ? intimation.irdaHospCode : ""));
                    $('#prev_clm_intComments').html((intimation.comments ? intimation.comments : ""));
                    $('#prev_clm_intSmCode').html((intimation.smCode ? intimation.smCode : ""));
                    $('#prev_clm_intSmName').html((intimation.smName ? intimation.smName : ""));
                    $('#prev_clm_intBrokerCode').html((intimation.brokerCode ? intimation.brokerCode : ""));
                    $('#prev_clm_intBrokerName').html((intimation.brokerName ? intimation.brokerName : ""));
                }
                if (data.claim) {
                    var claim = data.claim;
                    $('#prev_clm_No').html((claim.claimId ? claim.claimId : ""));
                    $('#prev_clm_provisionNo').html((claim.provisionAmount ? claim.provisionAmount : ""));
                    $('#prev_clm_regStatus').html((claim.registrationRemarks ? claim.registrationRemarks : ""));
                    $('#prev_clm_clmType').html((claim.claimType ? claim.claimType.value : ""));
                    $('#prev_clm_currency').html((claim.currencyId ? claim.currencyId.value : ""));
                    $('#prev_clm_remarks').html((claim.registrationRemarks ? claim.registrationRemarks : ""));
                }


                $('.prev_clm_billing_tbody').html('');
                if (data.billing) {
                    var billing = data.billing;
                    for (var i = 0; i < billing.length; i++) {
                        $('.prev_clm_billing_tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (billing[i].rodNumber ? billing[i].rodNumber : '') + '</td><td>' + (billing[i].claimType ? billing[i].claimType : '') + '</td><td>' + (billing[i].billClassification ? billing[i].billClassification : '') + '</td><td>' + (billing[i].docReceivedFrom ? billing[i].docReceivedFrom : '') + '</td><td>' + (billing[i].rodType ? billing[i].rodType : '') + '</td><td>' + (billing[i].billingDate ? billing[i].billingDate : '') + '</td><td>' + (billing[i].billAssessmentAmt ? billing[i].billAssessmentAmt : '') + '</td><td>' + (billing[i].status ? billing[i].status : '') + '</td><td class="hreflink prevBillDetailClick" data-link="subPage" data-rodKey = "' + (billing[i].rodKey ? billing[i].rodKey : '') + '">View Bill Details</td><td class="hreflink prevBillClick" data-link="subPage">View Bill Summary</td></tr>');
                    }
                }

                $('.prev_clm_payment_tbody').html('');
                if (data.payments) {
                    var payments = data.payments;
                    for (var i = 0; i < payments.length; i++) {
                        $('.prev_clm_payment_tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (payments[i].rodNo ? payments[i].rodNo : '') + '</td><td>' + (payments[i].claimType ? payments[i].claimType : '') + '</td><td>' + (payments[i].billClassification ? payments[i].billClassification : '') + '</td><td>' + (payments[i].docReceivedFrom ? payments[i].docReceivedFrom : '') + '</td><td>' + (payments[i].rodType ? payments[i].rodType : '') + '</td><td>' + (payments[i].amount ? payments[i].amount : '') + '</td><td>' + (payments[i].paymentType ? payments[i].paymentType : '') + '</td><td>' + (payments[i].bankName ? payments[i].bankName : '') + '</td><td>' + (payments[i].accountNumber ? payments[i].accountNumber : '') + '</td><td>' + (payments[i].ifscCode ? payments[i].ifscCode : '') + '</td><td>' + (payments[i].branchName ? payments[i].branchName : '') + '</td><td>' + (payments[i].chequeDateValue ? payments[i].chequeDateValue : '') + '</td><td>' + (payments[i].chequeNo ? payments[i].chequeNo : '') + '</td><td data-link="subPage" class="hreflink prev_payment_click" data-rodNo="' + (payments[i].rodNo ? payments[i].rodNo : '') + '" data-claimType="' + (payments[i].claimType ? payments[i].claimType : '') + '">View Details</td></tr>');
                    }
                }

                $('.prev_clm_financial_tbody').html('');
                if (data.finance) {
                    var finance = data.finance;
                    for (var i = 0; i < finance.length; i++) {
                        $('.prev_clm_financial_tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (finance[i].rodNumber ? finance[i].rodNumber : '') + '</td><td>' + (finance[i].typeOfClaim ? finance[i].typeOfClaim : '') + '</td><td>' + (finance[i].billClassification ? finance[i].billClassification : '') + '</td><td>' + (finance[i].documentReceivedFrom ? finance[i].documentReceivedFrom : '') + '</td><td>' + (finance[i].rodType ? finance[i].rodType : '') + '</td><td>' + (finance[i].faDate ? finance[i].faDate : '') + '</td><td>' + (finance[i].amount ? finance[i].amount : '') + '</td><td>' + (finance[i].status ? finance[i].status : '') + '</td><td>' + (finance[i].financialRemarks ? finance[i].financialRemarks : '') + '</td></tr>');
                    }
                }
                $('.prev_clm_medical_tbody').html('');
                if (data.medical) {
                    var medical = data.medical;
                    for (var i = 0; i < medical.length; i++) {
                        $('.prev_clm_medical_tbody').append('<tr data-json="" id="prev_ack_' + (i + 1) + '"><td>' + (i + 1) + '</td><td>' + (medical[i].rodNumber ? medical[i].rodNumber : '') + '</td><td>' + (medical[i].claimType ? medical[i].claimType.value : '') + '</td><td>' + (medical[i].billClassification ? medical[i].billClassification : '') + '</td><td>' + (medical[i].receivedFromValue ? medical[i].receivedFromValue : '') + '</td><td>' + (medical[i].rodType ? medical[i].rodType : '') + '</td><td>' + (medical[i].strDocumentReceivedDate ? medical[i].strDocumentReceivedDate : '') + '</td><td>' + (medical[i].strLastDocumentReceivedDate ? medical[i].strLastDocumentReceivedDate : '') + '</td><td>' + (medical[i].medicalResponseTime ? medical[i].medicalResponseTime : '') + '</td><td>' + (medical[i].modeOfRececipt ? medical[i].modeOfRececipt : '') + '</td><td>' + (medical[i].totalBillAmount ? medical[i].totalBillAmount : '') + '</td><td>' + (medical[i].approvedAmount ? medical[i].approvedAmount : '') + '</td><td>' + (medical[i].status ? medical[i].status : '') + '</td><td class="hreflink prevClmTrailsClk" data-link="subPage" data-intKey="' + (medical[i].claim ? medical[i].claim.intimation.key : '') + '"   data-rodKey="' + (medical[i].rodKey ? medical[i].rodKey : '') + '">View Trails</td><td data-rodKey="' + (medical[i].rodKey ? medical[i].rodKey : '') + '" class="hreflink prevClmDocAcknowledgementClk" data-link="subPage" >View Acknowledgement</td><td class="hreflink rejLinkClick" data-link="subPage" data-rodNo="' + (medical[i].rodNumber ? medical[i].rodNumber : '') + '">View Rejection Details</td><td class="hreflink prevQueryClick" data-claimKey="' + (medical[i].claim ? medical[i].claim.key : '') + '" data-link="subPage" data-rodKey="' + (medical[i].rodKey ? medical[i].rodKey : '') + '">View Query Details</td><td class="hreflink viewMedicalDetails" data-link="subPage" data-rodKey="' + (medical[i].rodKey ? medical[i].rodKey : '') + '" >View Medical Summary</td><td class="hreflink viewClosureDetails" data-intimationNo="' + (medical[i].intimationNumber ? medical[i].intimationNumber : '') + '" data-rodNo="' + (medical[i].rodNumber ? medical[i].rodNumber : '') + '" data-rodKey="' + (medical[i].rodKey ? medical[i].rodKey : '') + '">View Closure Details(ROD Level)</td></tr>');
                        $("#prev_ack_" + (i + 1)).attr('data-json', JSON.stringify(medical[i]));
                    }
                }

                $('.prev_clm_preauth_tbody').html('');
                if (data.preauth) {
                    var preauth = data.preauth;
                    if (preauth.length > 0) {
                        for (var i = 0; i < preauth.length; i++) {
                            $('.prev_clm_preauth_tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (preauth[i].referenceNo ? preauth[i].referenceNo : '') + '</td><td>' + (preauth[i].referenceType ? preauth[i].referenceType : '') + '</td><td>' + (preauth[i].treatementType ? preauth[i].treatementType : '') + '</td><td>' + (preauth[i].requestedAmt ? preauth[i].requestedAmt : '') + '</td><td>' + (preauth[i].approvedAmt ? preauth[i].approvedAmt : '') + '</td><td class="hreflink prev_clm_preauth" data-link="subPage" data-id="' + (preauth[i].key ? preauth[i].key : '') + '" data-value="' + (preauth[i].referenceNo ? preauth[i].referenceNo : '') + '">View Details</td></tr>');
                        }
                        $('.prev_clm_preauth_tbody').append('<tr class="subtotal"><td></td><td></td><td></td><td>Total Amount</td><td>' + (data.requestedAmt ? data.requestedAmt : '') + '</td><td>' + (data.totalApprovedAmt ? data.totalApprovedAmt : '') + '</td><td></td></tr>');
                    }
                }

                $('.prev_clm_diagnosis_tbody').html('');
                if (data.diagnosis) {
                    var diagnosis = data.diagnosis;
                    for (var i = 0; i < diagnosis.length; i++) {
                        $('.prev_clm_diagnosis_tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (diagnosis[i].diagnosis ? diagnosis[i].diagnosis : '') + '</td><td>' + (diagnosis[i].icdChapterValue ? diagnosis[i].icdChapterValue : '') + '</td><td>' + (diagnosis[i].icdCodeValue ? diagnosis[i].icdCodeValue : '') + '</td><td>' + (diagnosis[i].icdBlockValue ? diagnosis[i].icdBlockValue : '') + '</td><td>' + (diagnosis[i].sublimitApplicableValue ? diagnosis[i].sublimitApplicableValue : '') + '</td><td>' + (diagnosis[i].sublimitNameValue ? diagnosis[i].sublimitNameValue : '') + '</td><td>' + (diagnosis[i].subLimitAmount ? diagnosis[i].subLimitAmount : '') + '</td><td>' + (diagnosis[i].considerForPaymentValue ? diagnosis[i].considerForPaymentValue : '') + '</td><td>' + (diagnosis[i].sumInsuredRestriction ? diagnosis[i].sumInsuredRestriction : '') + '</td></tr>');
                    }
                }

                $('.prev_clm_sec_tbody').html('');
                if (data.section) {
                    $('.prev_clm_sec_tbody').append('<tr><td>' + (data.section ? data.section.section : '') + '</td><td>' + (data.section ? data.section.cover : '') + '</td><td>' + (data.section ? data.section.subCover : '') + '</td></tr>');
                }

                $('.prev_clm_fvr_tbody').html('');
                if (data.fieldVisit) {
                    var fieldVisit = data.fieldVisit;
                    for (var i = 0; i < fieldVisit.length; i++) {
                        $('.prev_clm_fvr_tbody').append('<tr><td>' + (i + 1) + '</td><td>' + (fieldVisit[i].representiveName ? fieldVisit[i].representiveName : '') + '</td><td>' + (fieldVisit[i].remarks ? fieldVisit[i].remarks : '') + '</td><td>' + (fieldVisit[i].fvrAssignedDate ? fieldVisit[i].fvrAssignedDate : '') + '</td><td>' + (fieldVisit[i].fvrReceivedDate ? fieldVisit[i].fvrReceivedDate : '') + '</td><td>' + (fieldVisit[i].status ? fieldVisit[i].status : '') + '</td></tr>');
                    }
                }

                $('#prev_clm_cashless_status').val((data.cashlessStatus ? data.cashlessStatus : ''));
                $('#prev_clm_app_amt').val((data.totalApprovedAmt ? data.totalApprovedAmt : ''));

                document.getElementsByClassName('previous_claim_status_dialog')[0].style.display = "block";
                $('.previous_claim_status_dialog').addClass('dialogPosition');
            } else if (xhr.status == 204) {
                alert("Details not found");
            } else if (xhr.status == 400) {
                alert("Bad Request");
            }
            $('.inner-overlay').hide();
            $('.overlay').hide();
        });
    }
    return false;
});

jQuery(document).on("click", '.fileNotFoundBtn', function() {
    var message = $(this).attr('data-message');
    if (message) {
        alert(message);
    }
});

jQuery(document).on("click", '.earlierDocButton', function() {
    var url = $(this).attr('data-url');
    if (url) {
        window.open(url, "popUp", "resizable=yes");
    }
});